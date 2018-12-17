package com.test.baselibrary.video.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.test.baselibrary.R;
import com.test.baselibrary.Utils.LogUtils;
import com.test.baselibrary.Utils.Utils;
import com.test.baselibrary.video.AdParameters;
import com.test.baselibrary.video.constant.SDKConstant;

/**
 * @createTime: 2018/10/15
 * @author: lady_zhou
 * @Description: 负责video播放，暂停，事件触发
 */
public class CustomVideoView extends RelativeLayout implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, TextureView.SurfaceTextureListener {

    /**
     * 常量
     */
    private static final String TAG = "MraidVideoView";
    private static final int TIME_MSG = 0x01;
    private static final int TIME_INVAL = 1000;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSING = 2;
    //重试机制次数
    private static final int LOAD_TOTAL_COUNT = 3;

    /**
     * ui
     */
    private ViewGroup mParentContainer;
    private RelativeLayout mPlayerView;
    private TextureView mVideoView;
    private Button mMiniPlayBtn;
    private ImageView mFullBtn;
    private ImageView mLoadingBar;
    private ImageView mFrameView;
    private AudioManager audioManager; //音量控制器
    private Surface videoSurface;//真正显示帧数据的类

    /**
     * Data
     */
    private String mUrl; //要加载的视频地址
    private String mFrameURI;
    private boolean isMute;
    private int mScreenWidth, mDestationHeight; //宽是屏宽，，高是按16：9的高度计算出来

    /**
     * Status状态保护
     */
    private boolean canPlay = true;
    private boolean mIsRealPause; //播放器是否真正的暂停
    private boolean mIsComplete;//播放器是否真正的完成
    private int mCurrentCount;
    private int playerState = STATE_IDLE; //默认处于空闲状态

    private MediaPlayer mediaPlayer;
    private ADVideoPlayerListener listener; //事件监听回调
    private ScreenEventReceiver mScreenReceiver; //监听屏幕是否锁屏

    private ADFrameImageLoadListener mFrameLoadListener;

    //隔一秒发送一次，handle复用主线程去发送事件
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:
                    if (isPlaying()) {
                        //还可以在这里更新progressbar
                        //LogUtils.i(TAG, "TIME_MSG");
                        listener.onBufferUpdate(getCurrentPosition());
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INVAL);
                    }
                    break;
            }
        }
    };

    /**
     * @return : int
     * @date 创建时间: 2018/10/18
     * @author lady_zhou
     * @Description 获取当前的播放位置
     */
    public int getCurrentPosition() {
        if (this.mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * @return : int
     * @date 创建时间: 2018/10/18
     * @author lady_zhou
     * @Description 获取文件时间
     */
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public CustomVideoView(Context context, ViewGroup parentContainer) {
        super(context);
        mParentContainer = parentContainer;
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        initData();
        initView();
        registerBroadcastReceiver();
    }

    private void initData() {
//        DisplayMetrics类获取屏幕的宽高和密度。
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mDestationHeight = (int) (mScreenWidth * SDKConstant.VIDEO_HEIGHT_PERCENT);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        mPlayerView = (RelativeLayout) inflater.inflate(R.layout.xadsdk_video_player, this);
        mVideoView = mPlayerView.findViewById(R.id.xadsdk_player_video_textureView);
        mVideoView.setOnClickListener(this);
        mVideoView.setKeepScreenOn(true);
        mVideoView.setSurfaceTextureListener(this);
        initSmallLayoutMode(); //init the small mode
    }

    public boolean isRealPause() {
        return mIsRealPause;
    }

    public boolean isComplete() {
        return mIsComplete;
    }

    public void setDataSource(String url) {
        this.mUrl = url;
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 小屏模式状态
     */
    private void initSmallLayoutMode() {
        LayoutParams params = new LayoutParams(mScreenWidth, mDestationHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPlayerView.setLayoutParams(params);

        mMiniPlayBtn = mPlayerView.findViewById(R.id.xadsdk_small_play_btn);
        mFullBtn = mPlayerView.findViewById(R.id.xadsdk_to_full_view);
        mLoadingBar = mPlayerView.findViewById(R.id.loading_bar);
        mFrameView = mPlayerView.findViewById(R.id.framing_view);
        mMiniPlayBtn.setOnClickListener(this);
        mFullBtn.setOnClickListener(this);
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 注册广播接收器
     */
    private void registerBroadcastReceiver() {
        if (mScreenReceiver == null) {
            mScreenReceiver = new ScreenEventReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            getContext().registerReceiver(mScreenReceiver, filter);
        }
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 销毁广播注册器
     */
    private void unRegisterBroadcastReceiver() {
        if (mScreenReceiver != null) {
            getContext().unregisterReceiver(mScreenReceiver);
        }
    }

    /**
     * @author lady_zhou
     * @createTime: 2018/10/15
     * @Description 监听锁屏事件的广播接收器
     */
    private class ScreenEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //主动锁屏时 pause, 主动解锁屏幕时，resume
            switch (intent.getAction()) {
                case Intent.ACTION_USER_PRESENT:
                    if (playerState == STATE_PAUSING) {
                        if (mIsRealPause) {
                            //手动点的暂停，回来后还暂停
                            pause();
                        } else {
                            decideCanPlay();
                        }
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    if (playerState == STATE_PLAYING) {
                        pause();
                    }
                    break;
            }
        }
    }

    private void decideCanPlay() {
        if (Utils.getVisiblePercent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT)
            //来回切换页面时，只有 >50,且满足自动播放条件才自动播放
            resume();
        else
            pause();
    }

    /**
     * @return : boolean
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 判断播放器是否播放
     */
    public boolean isPlaying() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == this.mMiniPlayBtn) {
            if (this.playerState == STATE_PAUSING) {
                if (Utils.getVisiblePercent(mParentContainer)
                        > SDKConstant.VIDEO_SCREEN_PERCENT) {
                    resume();
                    this.listener.onClickPlay();
                }
            } else {
                load();
            }
        } else if (v == this.mFullBtn) {
            this.listener.onClickFullScreenBtn();
        } else if (v == mVideoView) {
            this.listener.onClickVideo();
        }
    }

    /**
     * @param mp :
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 采用异步的方法，准备接口的监听，在此可以调用播放视频
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        showPlayView();
        mediaPlayer = mp;
        if (mediaPlayer != null) {
            //设置监听
            mediaPlayer.setOnBufferingUpdateListener(this);
            mCurrentCount = 0;
            if (listener != null) {
                listener.onAdVideoLoadSuccess();
            }

            //满足自动播放条件，则直接播放
            if (Utils.canAutoPlay(getContext(),
                    AdParameters.getCurrentSetting()) &&
                    Utils.getVisiblePercent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
                setCurrentPlayState(STATE_PAUSING);
                resume();
            } else {
                setCurrentPlayState(STATE_PLAYING);
                pause();
            }
        }
    }

    private void showPlayView() {
        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(View.GONE);
        mMiniPlayBtn.setVisibility(View.GONE);
        mFrameView.setVisibility(View.GONE);
    }

    /**
     * @param mp    :
     * @param what  :
     * @param extra :
     * @return : boolean  返回true表示自己处理异常，，，false是默认值
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 捕获异常的错误
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        this.playerState = STATE_ERROR;
        mediaPlayer = mp;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        if (mCurrentCount >= LOAD_TOTAL_COUNT) {
            showPauseView(false);
            if (this.listener != null) {
                listener.onAdVideoLoadFailed();
            }
        }
        this.stop();//去重新load
        return true;
    }

    /**
     * @param mp :
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 视频播放完成后，进行视频处理
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (listener != null) {
            listener.onAdVideoLoadComplete();
        }
        setIsComplete(true);
        setIsRealPause(true);
        playBack();
    }

    /**
     * @param mp    :
     * @param what  :
     * @param extra :
     * @return : boolean
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description
     */
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return true;
    }

    /**
     * @param changedView :
     * @param visibility  :
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 屏幕状态发生改变回调
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * @param mp      :
     * @param percent :
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 缓存视频时调用
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

//    --------------------------------------TextureView.SurfaceTextureListener 周期回调------------------------------------

    /**
     * @param surface :
     * @param width   :
     * @param height  :
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 表明我们的TextureView处于就绪
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        videoSurface = new Surface(surface);
        checkMediaPlayer();
        mediaPlayer.setSurface(videoSurface);
        load();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 加载我们的视频url
     */
    public void load() {
        if (this.playerState != STATE_IDLE) {
            return;
        }
        try {
            showLoadingView();
            //将当前状态设成空闲态
            setCurrentPlayState(STATE_IDLE);
            //完成视频的创建工作
            checkMediaPlayer();

            mediaPlayer.setDataSource(this.mUrl);
            mediaPlayer.prepareAsync(); //开始异步加载
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            stop();
        }

    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/16
     * @author lady_zhou
     * @Description 检查播放器是否为空，并完成视频的创建工作
     */
    private void checkMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = createMediaPlayer();//每次都重新创建一个新的播放器
        }
    }

    /**
     * @return : android.media.MediaPlayer
     * @date 创建时间: 2018/10/16
     * @author lady_zhou
     * @Description 创建新的播放器
     */
    private MediaPlayer createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();

        //进行监听设置
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnErrorListener(this);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (videoSurface != null && videoSurface.isValid()) {
            mediaPlayer.setSurface(videoSurface);
        } else {
            stop();
        }
        return mediaPlayer;
    }

    /**
     * @param stateIdle :  状态值
     * @return : void
     * @date 创建时间: 2018/10/16
     * @author lady_zhou
     * @Description 设置播放器状态状态
     */
    private void setCurrentPlayState(int stateIdle) {
        playerState = stateIdle;
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/16
     * @author lady_zhou
     * @Description 显示加载页面
     */
    private void showLoadingView() {
        mFullBtn.setVisibility(View.GONE);
        mLoadingBar.setVisibility(View.VISIBLE);

        AnimationDrawable anim = (AnimationDrawable) mLoadingBar.getBackground();
        anim.start();

        mMiniPlayBtn.setVisibility(View.GONE);
        mFrameView.setVisibility(View.GONE);
        loadFrameImage();
    }

    /**
     * @param show : 是否显示
     * @return : void
     * @date 创建时间: 2018/10/16
     * @author lady_zhou
     * @Description 显示暂停画面
     */
    private void showPauseView(boolean show) {
        mFullBtn.setVisibility(show ? View.VISIBLE : View.GONE);
        mMiniPlayBtn.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(View.GONE);
        if (!show) {
            mFrameView.setVisibility(View.VISIBLE);
            loadFrameImage();
        } else {
            mFrameView.setVisibility(View.GONE);
        }
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/16
     * @author lady_zhou
     * @Description 异步加载定帧图
     */
    private void loadFrameImage() {
        if (mFrameLoadListener != null) {
            mFrameLoadListener.onStartFrameLoad(mFrameURI, new ImageLoaderListener() {
                @Override
                public void onLoadingComplete(Bitmap loadedImage) {
                    if (loadedImage != null) {
                        mFrameView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mFrameView.setImageBitmap(loadedImage);
                    } else {
                        mFrameView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        mFrameView.setImageResource(R.drawable.xadsdk_img_error);
                    }
                }
            });
        }
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 暂停视频
     */
    public void pause() {
        if (this.playerState != STATE_PLAYING) {
            return;
        }
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            mediaPlayer.pause();
            if (!this.canPlay) {
                this.mediaPlayer.seekTo(0);
            }
        }
        this.showPauseView(false);
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 恢复视频播放
     */
    public void resume() {
        if (this.playerState != STATE_PAUSING) {
            return;
        }

        if (!isPlaying()) {
            entryResumeState();//置为播放中的状态值
            mediaPlayer.setOnSeekCompleteListener(null);
            mediaPlayer.start();
            mHandler.sendEmptyMessage(TIME_MSG);
            showPauseView(true);
        } else {
            showPauseView(true);
        }
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/18
     * @author lady_zhou
     * @Description 进入播放状态时的状态更新，将变量设置为播放状态中应处于的播放状态值
     */
    private void entryResumeState() {
        canPlay = true;
        setCurrentPlayState(STATE_PLAYING);
        setIsRealPause(false);
        setIsComplete(false);
    }

    public boolean isFrameHidden() {
        return mFrameView.getVisibility() == View.VISIBLE ? false : true;
    }

    public void setIsComplete(boolean isComplete) {
        mIsComplete = isComplete;
    }

    /**
     * @param isRealPause :
     * @return : void
     * @date 创建时间: 2018/10/18
     * @author lady_zhou
     * @Description 设置标志位
     */
    public void setIsRealPause(boolean isRealPause) {
        this.mIsRealPause = isRealPause;
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 播放完成后回到初始状态，跳回第一帧
     */
    public void playBack() {
        setCurrentPlayState(STATE_PAUSING);
        mHandler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            mediaPlayer.setOnSeekCompleteListener(null);
            mediaPlayer.seekTo(0);  //设置成0
            mediaPlayer.pause();
        }
        this.showPauseView(false);
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 停止状态
     */
    public void stop() {
        //清空mediaPlayer
        if (this.mediaPlayer != null) {
            this.mediaPlayer.reset();
            this.mediaPlayer.setOnSeekCompleteListener(null);
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        mHandler.removeCallbacksAndMessages(null);
        setCurrentPlayState(STATE_IDLE);

        if (mCurrentCount < LOAD_TOTAL_COUNT) { //满足重新加载的条件
            mCurrentCount += 1;
            load();
        } else {
            showPauseView(false); //显示暂停状态
        }
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 销毁我们当前的自定义view
     */
    public void destroy() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.setOnSeekCompleteListener(null);
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        setCurrentPlayState(STATE_IDLE);

        mCurrentCount = 0;
        setIsComplete(false);
        setIsRealPause(false);
        unRegisterBroadcastReceiver();
        mHandler.removeCallbacksAndMessages(null); //release all message and runnable
        showPauseView(false); //除了播放和loading外其余任何状态都显示pause
    }

    /**
     * @param position :  播放的位置
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 跳转大小屏并播放视频
     */
    public void seekAndResume(int position) {
        if (mediaPlayer != null) {
            showPauseView(true);
            entryResumeState();
            mediaPlayer.seekTo(position);
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mediaPlayer.start();
                    mHandler.sendEmptyMessage(TIME_MSG);
                }
            });
        }
    }

    /**
     * @param position :  播放的位置
     * @return : void
     * @date 创建时间: 2018/10/15
     * @author lady_zhou
     * @Description 跳转大小屏并暂停视频
     */
    public void seekAndPause(int position) {
        if (this.playerState != STATE_PLAYING) {
            return;
        }
        showPauseView(false);
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            mediaPlayer.seekTo(position);
            //seekTo的事件监听
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    //跳转事件结束后，暂停视频
                    mediaPlayer.pause();
                    mHandler.removeCallbacksAndMessages(null);
                }
            });
        }
    }

    /**
     * @param isShow : true  为显示，false  为不显示
     * @return : void
     * @date 创建时间: 2018/10/29
     * @author lady_zhou
     * @Description 是否显示全屏按钮
     */
    public void isShowFullBtn(boolean isShow) {
        mFullBtn.setImageResource(isShow ? R.drawable.xadsdk_ad_mini : R.drawable.xadsdk_ad_mini_null);
        mFullBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    //全屏不显示暂停状态,后续可以整合，不必单独出一个方法
    public void pauseForFullScreen() {
        if (playerState != STATE_PLAYING) {
            return;
        }
        LogUtils.d(TAG, "do full pause");
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            mediaPlayer.pause();
            if (!this.canPlay) {
                mediaPlayer.seekTo(0);
            }
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * @param mute : true是没有声音的
     * @return : void
     * @date 创建时间: 2018/10/25
     * @author lady_zhou
     * @Description 静音
     */
    public void mute(boolean mute) {
        isMute = mute;
        if (mediaPlayer != null && this.audioManager != null) {
            float volume = isMute ? 0.0f : 1.0f;
            //调节当前程序的音量
            mediaPlayer.setVolume(volume, volume);
        }
    }

    public interface ADFrameImageLoadListener {
        void onStartFrameLoad(String url, ImageLoaderListener listener);
    }

    /**
     * @author lady_zhou
     * @createTime: 2018/10/24
     * @Description 供slot层来实现具体点击逻辑, 具体逻辑还会变，如果对UI的点击没有具体监测的话可以不回调
     */
    public interface ADVideoPlayerListener {

        void onBufferUpdate(int time);

        void onClickFullScreenBtn();

        void onClickVideo();

        void onClickBackBtn();

        void onClickPlay();

        void onAdVideoLoadSuccess();

        void onAdVideoLoadFailed();

        void onAdVideoLoadComplete();
    }

    public interface ImageLoaderListener {
        /**
         * 如果图片下载不成功，传null
         *
         * @param loadedImage
         */
        void onLoadingComplete(Bitmap loadedImage);
    }

    public void setListener(ADVideoPlayerListener listener) {
        this.listener = listener;
    }

    public void setFrameLoadListener(ADFrameImageLoadListener frameLoadListener) {
        this.mFrameLoadListener = frameLoadListener;
    }

    public void setFrameURI(String url) {
        mFrameURI = url;
    }
}
