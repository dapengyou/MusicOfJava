package com.test.musicofjava.manager;

import android.content.Context;
import android.media.AudioManager;

import com.test.musicofjava.service.AudioPlayer;

import static android.content.Context.AUDIO_SERVICE;

/**
 * audio焦点管理器
 */
public class AudioFocusManager implements AudioManager.OnAudioFocusChangeListener {
    private AudioManager mAudioManager;
    private boolean isPausedByFocusLossTransient;

    public AudioFocusManager(Context context) {
        mAudioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    /**
     * 获取焦点
     *
     * @return
     */
    public boolean requestAudioFous() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.requestAudioFocus(this,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
    }

    /**
     * 释放焦点
     */
    public void abandonAudioFocus() {
        mAudioManager.abandonAudioFocus(this);
    }

    /**
     * 处理丢失焦点
     *
     * @param focusChange
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            // 重新获得焦点
            case AudioManager.AUDIOFOCUS_GAIN:
                if (isPausedByFocusLossTransient) {
                    // 通话结束，恢复播放
                    AudioPlayer.getInstance().startPlayer();
                }

                // 恢复音量
                AudioPlayer.getInstance().getMediaPlayer().setVolume(1f, 1f);

                isPausedByFocusLossTransient = false;
                break;
            // 永久丢失焦点，如被其他播放器抢占
            case AudioManager.AUDIOFOCUS_LOSS:
                AudioPlayer.getInstance().pausePlayer();
                break;
            // 短暂丢失焦点，如来电
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                AudioPlayer.getInstance().pausePlayer(false);
                isPausedByFocusLossTransient = true;
                break;
            // 瞬间丢失焦点，如通知
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // 音量减小为一半
                AudioPlayer.getInstance().getMediaPlayer().setVolume(0.5f, 0.5f);
                break;
        }
    }
}
