package com.test.musicofjava.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.test.baselibrary.Utils.DateUtils;
import com.test.baselibrary.Utils.StatusBarUtil;
import com.test.baselibrary.bean.Music;
import com.test.musicofjava.MyBaseActivity;
import com.test.musicofjava.R;
import com.test.musicofjava.dataSave.SharedPreferencesManager;
import com.test.musicofjava.enums.PlayModeEnum;
import com.test.musicofjava.fragment.AlbumFragment;
import com.test.musicofjava.fragment.LrcViewFragment;
import com.test.musicofjava.service.AudioPlayer;
import com.test.musicofjava.service.OnPlayerEventListener;
import com.test.musicofjava.utils.FileUtils;
import com.test.musicofjava.widget.AlbumCoverView;
import com.test.musicofjava.widget.LyricView;

import java.io.File;

public class PlayActivity extends MyBaseActivity implements OnPlayerEventListener, SeekBar.OnSeekBarChangeListener {
    private ImageView mIvBack;
    private ImageView mIvNext, mIvPlay, mIvPrev, mIvMode;

    private TextView mTvCurrentTime;//当前播放的时间
    private TextView mTvTotalTime;//歌曲总时间

    private TextView mTvTitle;
    private TextView mTvArtist;

    private SeekBar mSeekBar;
    private int mLastProgress;
    private boolean isDraggingProgress;//是否拖动进度条


    private View mFrontAlbum;//专辑封面
    private View mBackLrc;//歌词
    private View mFrameLayout;
    private LrcViewFragment mLrcViewFragment;
    private AlbumFragment mAlbumFragment;
    private boolean isShowLrc = false;
    private AlbumCoverView mAlbumCoverView;
    private boolean isPlay = false;//是否播放
    private LyricView mLyricView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //切换按钮
        mIvBack = findViewById(R.id.iv_back);
        mIvNext = findViewById(R.id.iv_next);
        mIvMode = findViewById(R.id.iv_mode);
        mIvPlay = findViewById(R.id.iv_play);
        mIvPrev = findViewById(R.id.iv_prev);

        //歌曲时间显示
        mTvCurrentTime = findViewById(R.id.tv_current_time);
        mTvTotalTime = findViewById(R.id.tv_total_time);

        mSeekBar = findViewById(R.id.seekbar);

        //封面、歌词
        mFrameLayout = findViewById(R.id.frameLayout);
        mFrontAlbum = findViewById(R.id.ll_front_album);
        mBackLrc = findViewById(R.id.ll_back_lrc);
        mAlbumCoverView = findViewById(R.id.albumCoverView);
        mLyricView = findViewById(R.id.lyric_view);

        mTvTitle = findViewById(R.id.tv_title);
        mTvArtist = findViewById(R.id.tv_artist);

        //沉浸式状态栏
        StatusBarUtil.setTranslucent(this, 159);

    }

    @Override
    protected void initData(Intent intent, Bundle savedInstanceState) {
        initPlayMode();
        flipCard();
        initLyric();

        initMusic(AudioPlayer.getInstance().getPlayMusic());
        AudioPlayer.getInstance().addOnPlayEventListener(this);
    }

    private void initLyric() {
        mLyricView.setLineSpace(8.0f);
        mLyricView.setTextSize(10.0f);
        mLyricView.setCurrentTextSize(15.0f);
        mLyricView.setPlayable(true);
        mLyricView.setTouchable(true);
        mLyricView.setClickable(true);
    }

    private void initMusic(Music music) {
        if (music == null) {
            return;
        }

        mTvTitle.setText(music.getTitle());
        mTvArtist.setText(music.getArtist());
        mSeekBar.setProgress((int) AudioPlayer.getInstance().getAudioPosition());
        mSeekBar.setSecondaryProgress(0);
        mSeekBar.setMax((int) music.getDuration());
        mLastProgress = 0;
        mTvCurrentTime.setText("00:00");
        mTvTotalTime.setText(DateUtils.format("mm:ss", music.getDuration()));
//        setCoverAndBg(music);
        setLrc(music);
        //是否正在播放，或是正在准备
        if (AudioPlayer.getInstance().isPlaying() || AudioPlayer.getInstance().isPreparing()) {
            mIvPlay.setSelected(true);
            mAlbumCoverView.start();
        } else {
            mIvPlay.setSelected(false);
            mAlbumCoverView.pause();
        }
    }

    @Override
    protected void initListener() {
        mIvPrev.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
        mIvMode.setOnClickListener(this);
        mIvNext.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

        mFrameLayout.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                AudioPlayer.getInstance().playPause();
                break;
            case R.id.iv_next:
                AudioPlayer.getInstance().next();
                break;
            case R.id.iv_prev:
                AudioPlayer.getInstance().prev();
                break;
            case R.id.iv_mode:
                setPlayMode();
                break;
            case R.id.iv_back:
                finish();
                this.overridePendingTransition(0, R.anim.anim_bottom_out);
                break;

            case R.id.frameLayout:
                isShowLrc = isShowLrc == false ? true : false;
                flipCard();
                break;


        }
    }

    /**
     * 设置歌词
     *
     * @param music
     */
    private void setLrc(final Music music) {
        //本地音乐
        if (music.getType() == Music.Type.LOCAL) {
            String lrcPath = FileUtils.getLrcFilePath(music);
            if (!TextUtils.isEmpty(lrcPath)) {
                loadLrc(lrcPath);
            } else {
                show("正在搜索歌词");
//                new SearchLrc(music.getArtist(), music.getTitle()) {
//                    @Override
//                    public void onPrepare() {
//                        // 设置tag防止歌词下载完成后已切换歌曲
//                        vpPlay.setTag(music);
//
//                        loadLrc("");
//                        setLrcLabel("正在搜索歌词");
//                    }
//
//                    @Override
//                    public void onExecuteSuccess(@NonNull String lrcPath) {
//                        if (vpPlay.getTag() != music) {
//                            return;
//                        }
//
//                        // 清除tag
//                        vpPlay.setTag(null);
//
//                        loadLrc(lrcPath);
//                        setLrcLabel("暂无歌词");
//                    }
//
//                    @Override
//                    public void onExecuteFail(Exception e) {
//                        if (vpPlay.getTag() != music) {
//                            return;
//                        }
//
//                        // 清除tag
//                        vpPlay.setTag(null);
//
//                        setLrcLabel("暂无歌词");
//                    }
//                }.execute();
            }
        } else {
            //线上音乐
            String lrcPath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
            loadLrc(lrcPath);
        }
    }

    /**
     * 加载歌词
     *
     * @param path
     */
    private void loadLrc(String path) {
        if (TextUtils.isEmpty(path)) {
            mLyricView.reset("暂无歌词");
        } else {
            File file = new File(path);
            mLyricView.setLyricFile(file, "UTF-8");
        }
    }


    //切换歌词与封面
    private void flipCard() {

        if (isShowLrc) {
            mBackLrc.setVisibility(View.VISIBLE);
            mFrontAlbum.setVisibility(View.GONE);
        } else {
            mBackLrc.setVisibility(View.GONE);
            mFrontAlbum.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 点击后设置播放模式变化
     */
    private void setPlayMode() {
        PlayModeEnum playModeEnum = PlayModeEnum.valueOf(SharedPreferencesManager.getInstance().getPlayMode());
        switch (playModeEnum) {
            //列表循环0
            case LOOP:
                playModeEnum = PlayModeEnum.SINGLE;
                show(playModeEnum.toString());
                break;
            //单曲循环1
            case SINGLE:
                playModeEnum = PlayModeEnum.SHUFFLE;
                show(playModeEnum.toString());
                break;
            //随机播放2
            case SHUFFLE:
                playModeEnum = PlayModeEnum.LOOP;
                show(playModeEnum.toString());
                break;
        }
        SharedPreferencesManager.getInstance().savePlayMode(playModeEnum.code());
        initPlayMode();
    }

    /**
     * 初始化播放模式
     */
    private void initPlayMode() {
        int mode = SharedPreferencesManager.getInstance().getPlayMode();
        mIvMode.setImageLevel(mode);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(0, R.anim.anim_bottom_out);
    }

    @Override
    public void onChangeMusic(Music music) {

    }

    @Override
    public void onPlayerStart() {
        mIvPlay.setSelected(true);
    }

    @Override
    public void onPlayerPause() {
        mIvPlay.setSelected(false);
    }

    @Override
    public void onPublish(int progress) {
        if (!isDraggingProgress) {
            mSeekBar.setProgress(progress);
        }
        mLyricView.setCurrentTimeMillis(progress);

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    //seekBar 监听实现的方法
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == mSeekBar) {
            if (Math.abs(progress - mLastProgress) >= android.text.format.DateUtils.SECOND_IN_MILLIS) {
                mTvCurrentTime.setText(DateUtils.format("mm:ss", progress));
                mLastProgress = progress;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar == mSeekBar) {
            isDraggingProgress = true;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == mSeekBar) {
            isDraggingProgress = false;
            if (AudioPlayer.getInstance().isPlaying() || AudioPlayer.getInstance().isPausing()) {
                int progress = seekBar.getProgress();
                AudioPlayer.getInstance().seekTo(progress);

//                if (mLrcViewSingle.hasLrc()) {
//                    mLrcViewSingle.updateTime(progress);
//                    mLrcViewFull.updateTime(progress);
//                }
            } else {
                seekBar.setProgress(0);
            }
        }
//        else if (seekBar == sbVolume) {
//            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(),
//                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
//        }
    }

}
