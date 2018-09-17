package com.test.musicofjava.service;

import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.test.baselibrary.Utils.ToastUtil;
import com.test.baselibrary.bean.Music;
import com.test.musicofjava.dataSave.SharedPreferencesManager;
import com.test.musicofjava.enums.PlayModeEnum;
import com.test.musicofjava.manager.AudioFocusManager;
import com.test.musicofjava.manager.MediaSessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hzwangchenyan on 2018/1/26.
 */
public class AudioPlayer {
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;

    private static final long TIME_UPDATE = 300L;

    private Context context;
    private AudioFocusManager audioFocusManager;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private IntentFilter noisyFilter;//意图过滤器
    private List<Music> musicList = new ArrayList<>();
    private final List<OnPlayerEventListener> listeners = new ArrayList<>();
    private int state = STATE_IDLE;
    private static AudioPlayer sInstance;

    public static AudioPlayer getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static AudioPlayer instance = new AudioPlayer();
    }


    private AudioPlayer() {
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context.getApplicationContext();
        audioFocusManager = new AudioFocusManager(context);
        mediaPlayer = new MediaPlayer();
        handler = new Handler(Looper.getMainLooper());
        //当耳机线被拔出，或者蓝牙耳机连接断开时，如果在播放音乐/视频，为了用户体验，避免突如其来的扬声器播放，我们通常做法是暂停此时正在播放的音乐/视频。
        //在这种情况下，系统会广播带有ACTION_AUDIO_BECOMING_NOISY的intent。我们只需要接受这种广播，对其进行处理即可。
        noisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

        initMediaPlayerListener();
    }

    private void initMediaPlayerListener() {
        //监听播放器的播放状态，网络流媒体播放结束时回调
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next();
            }
        });
        //当装载流媒体完毕的时候回调。
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (isPreparing()) {
                    startPlayer();
                }
            }
        });

        //网络流媒体的缓冲变化时回调
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
                for (OnPlayerEventListener listener : listeners) {
                    listener.onBufferingUpdate(percent);
                }
            }
        });
    }

    public void addOnPlayEventListener(OnPlayerEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeOnPlayEventListener(OnPlayerEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * 添加音乐
     *
     * @param music
     */
    public void addAndPlay(Music music) {
        int position = musicList.indexOf(music);
        if (position < 0) {
            musicList.add(music);
            position = musicList.size() - 1;
        }
        play(position);
    }

    /**
     * 播放
     *
     * @param position
     */
    public void play(int position) {
        if (musicList.isEmpty()) {
            return;
        }

        //1、确保position 从0 开始
        if (position < 0) {
            position = musicList.size() - 1;
        } else if (position >= musicList.size()) {
            position = 0;
        }
        //2、设置播放的位置
        setPlayPosition(position);
        //3、获取当前播放的音乐信息
        Music music = getPlayMusic();

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepareAsync();
            state = STATE_PREPARING;
            for (OnPlayerEventListener listener : listeners) {
                listener.onChangeMusic(music);
            }
//            MediaSessionManager.getInstance().updateMetaData(music);
//            MediaSessionManager.getInstance().updatePlaybackState();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showToast("当前歌曲无法播放");
        } catch (NullPointerException e) {
            e.printStackTrace();
            ToastUtil.showToast("请初始化MediaPlayer");
        }
    }

    public void delete(int position) {
        int playPosition = getPlayPosition();
        Music music = musicList.remove(position);
        if (playPosition > position) {
            setPlayPosition(playPosition - 1);
        } else if (playPosition == position) {
            if (isPlaying() || isPreparing()) {
                setPlayPosition(playPosition - 1);
                next();
            } else {
                stopPlayer();
                for (OnPlayerEventListener listener : listeners) {
                    listener.onChangeMusic(getPlayMusic());
                }
            }
        }
    }

    public void playPause() {
        if (isPreparing()) {
            stopPlayer();
        } else if (isPlaying()) {
            pausePlayer();
        } else if (isPausing()) {
            startPlayer();
        } else {
            play(getPlayPosition());
        }
    }

    public void startPlayer() {
        if (!isPreparing() && !isPausing()) {
            return;
        }

        if (audioFocusManager.requestAudioFous()) {
            mediaPlayer.start();
            state = STATE_PLAYING;
            handler.post(mPublishRunnable);
            MediaSessionManager.getInstance().updatePlaybackState();

            for (OnPlayerEventListener listener : listeners) {
                listener.onPlayerStart();
            }
        }
    }

    public void pausePlayer() {
        pausePlayer(true);
    }

    public void pausePlayer(boolean abandonAudioFocus) {
        if (!isPlaying()) {
            return;
        }

        mediaPlayer.pause();
        state = STATE_PAUSE;
        handler.removeCallbacks(mPublishRunnable);
        MediaSessionManager.getInstance().updatePlaybackState();
        if (abandonAudioFocus) {
            audioFocusManager.abandonAudioFocus();
        }

        for (OnPlayerEventListener listener : listeners) {
            listener.onPlayerPause();
        }
    }

    public void stopPlayer() {
        if (isIdle()) {
            return;
        }

        pausePlayer();
        mediaPlayer.reset();
        state = STATE_IDLE;
    }

    public void next() {
        if (musicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(0);
        switch (mode) {
            case SHUFFLE:
                play(new Random().nextInt(musicList.size()));
                break;
            case SINGLE:
                play(getPlayPosition());
                break;
            case LOOP:
            default:
                play(getPlayPosition() + 1);
                break;
        }
    }

    public void prev() {
        if (musicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(0);
        switch (mode) {
            case SHUFFLE:
                play(new Random().nextInt(musicList.size()));
                break;
            case SINGLE:
                play(getPlayPosition());
                break;
            case LOOP:
            default:
                play(getPlayPosition() - 1);
                break;
        }
    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPausing()) {
            mediaPlayer.seekTo(msec);
            MediaSessionManager.getInstance().updatePlaybackState();
            for (OnPlayerEventListener listener : listeners) {
                listener.onPublish(msec);
            }
        }
    }

    private Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying()) {
                for (OnPlayerEventListener listener : listeners) {
                    listener.onPublish(mediaPlayer.getCurrentPosition());
                }
            }
            handler.postDelayed(this, TIME_UPDATE);
        }
    };

    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    public long getAudioPosition() {
        if (isPlaying() || isPausing()) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public Music getPlayMusic() {
        if (musicList.isEmpty()) {
            return null;
        }
        return musicList.get(getPlayPosition());
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public boolean isPlaying() {
        return state == STATE_PLAYING;
    }

    public boolean isPausing() {
        return state == STATE_PAUSE;
    }

    public boolean isPreparing() {
        return state == STATE_PREPARING;
    }

    public boolean isIdle() {
        return state == STATE_IDLE;
    }

    /**
     * 获取播放位置
     *
     * @return
     */
    public int getPlayPosition() {
        int position = SharedPreferencesManager.getInstance().getPlayPosition();
        if (position < 0 || position >= musicList.size()) {
            position = 0;
            SharedPreferencesManager.getInstance().savePlayPosition(position);
        }
        return position;
    }

    private void setPlayPosition(int position) {
        SharedPreferencesManager.getInstance().savePlayPosition(position);
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }
}
