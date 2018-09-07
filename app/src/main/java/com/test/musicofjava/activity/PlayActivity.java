package com.test.musicofjava.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.test.baselibrary.base.BaseActivity;
import com.test.musicofjava.R;
import com.test.musicofjava.dataSave.SharedPreferencesManager;
import com.test.musicofjava.enums.PlayModeEnum;
import com.test.musicofjava.fragment.AlbumFragment;
import com.test.musicofjava.fragment.LrcViewFragment;

public class PlayActivity extends BaseActivity {
    private ImageView mIvBack;
    private ImageView mIvNext, mIvPlay, mIvPrev, mIvMode;

    private TextView mTvCurrentTime;//当前播放的时间
    private TextView mTvTotalTime;//歌曲总时间

    private SeekBar mSeekBar;

    private View mFrontAlbum;//专辑封面
    private View mBackLrc;//歌词
    private View mFrameLayout;
    private LrcViewFragment mLrcViewFragment;
    private AlbumFragment mAlbumFragment;
    private boolean isShowLrc = false;


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
//        mFrontAlbum = findViewById(R.id.ll_front_album);
//        mBackLrc = findViewById(R.id.ll_back_lrc);

    }

    @Override
    protected void initData(Intent intent, Bundle savedInstanceState) {
        initPlayMode();
        flipCard();
    }

    @Override
    protected void initListener() {
        mIvPrev.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
        mIvMode.setOnClickListener(this);
        mIvNext.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

        mFrameLayout.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                break;
            case R.id.iv_next:
                break;
            case R.id.iv_prev:
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

    //切换歌词与封面
    private void flipCard() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (isShowLrc) {
            if (mLrcViewFragment == null) {
                mLrcViewFragment = new LrcViewFragment();
                fragmentTransaction.replace(R.id.frameLayout, mLrcViewFragment);
            } else {
                fragmentTransaction.replace(R.id.frameLayout, mLrcViewFragment);
            }
        } else {
            if (mAlbumFragment == null) {
                mAlbumFragment = new AlbumFragment();
                fragmentTransaction.replace(R.id.frameLayout, mAlbumFragment);
            } else {
                fragmentTransaction.replace(R.id.frameLayout, mAlbumFragment);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();

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
}