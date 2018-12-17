package com.test.baselibrary.video.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.test.baselibrary.R;
import com.test.baselibrary.Utils.Utils;
import com.test.baselibrary.video.activity.AdBrowserActivity;
import com.test.baselibrary.video.bean.VideoBean;
import com.test.baselibrary.video.constant.SDKConstant;
import com.test.baselibrary.video.report.ReportManager;
import com.test.baselibrary.video.slot.VideoSlot;

/**
 * @createTime: 2018/10/25
 * @author: lady_zhou
 * @Description: 全屏显示视频
 */
public class VideoFullDialog extends Dialog implements CustomVideoView.ADVideoPlayerListener {
    private static final String TAG = VideoFullDialog.class.getSimpleName();

    private Context mContext;
    private CustomVideoView mCustomVideoView;
    private VideoBean mVideoBean;
    private int mPosition;

    private RelativeLayout mRootView;
    private ViewGroup mParentView;
    private ImageView mBackButton;
    private FullToSmallListener mListener;
    private boolean isFirst = true;
    //动画要执行的平移值
    private int deltaY;
    private VideoSlot.AdSDKSlotListener mSlotListener;
    private Bundle mStartBundle;
    private Bundle mEndBundle; //用于Dialog出入场动画

    public VideoFullDialog(@NonNull Context context, CustomVideoView customVideoView, VideoBean videoBean, int position) {
        super(context, R.style.dialog_full_screen); //通过style的设置，保证我的Dialog全屏
        this.mContext = context;
        this.mCustomVideoView = customVideoView;
        this.mVideoBean = videoBean;
        this.mPosition = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xadsdk_dialog_video_layout);
        initVideoView();
    }

    private void initVideoView() {
        mParentView = findViewById(R.id.content_layout);
        mBackButton = findViewById(R.id.xadsdk_player_close_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVideo();
            }
        });

        mRootView = findViewById(R.id.root_view);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVideo();
            }
        });
        mRootView.setVisibility(View.INVISIBLE);

        mCustomVideoView.setListener(this);//设置事件监听为当前的对话框
        mCustomVideoView.mute(false);//播放声音，设置静音
        mParentView.addView(mCustomVideoView);

        //视图将要绘制时调用该监听事件
        mParentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //获取到视图的宽度和高度后要移除该监听事件
                mParentView.getViewTreeObserver().removeOnPreDrawListener(this);
                prepareScene();
                runEnterAnimation();
                return true;
            }
        });
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/25
     * @author lady_zhou
     * @Description 准备入场动画
     */
    private void runEnterAnimation() {
        //View经常会需要集中动画混合在一起做，因此提供了一个ViewPropertyAnimator类来快速的实现多个动画的混合。
        mCustomVideoView.animate()
                .setDuration(200)
                .setInterpolator(new LinearInterpolator())
                .translationY(0)
                .withStartAction(new Runnable() {//动画开始时设置的行为
                    @Override
                    public void run() {
                        mRootView.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/25
     * @author lady_zhou
     * @Description 准备出场动画
     */
    private void runExitAnimator() {
        mCustomVideoView.animate()
                .setDuration(200)
                .setInterpolator(new LinearInterpolator())
                .translationY(deltaY)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {//动画结束时设置的行为
                        dismiss();
                        try {
                            ReportManager.exitfullScreenReport(mVideoBean.event.exitFull.content, mCustomVideoView.getCurrentPosition()
                                    / SDKConstant.MILLION_UNIT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mListener != null) {
                            mListener.getCurrentPlayPosition(mCustomVideoView.getCurrentPosition());
                        }
                    }
                }).start();
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/25
     * @author lady_zhou
     * @Description 准备动画所需数据
     */
    private void prepareScene() {
        mEndBundle = Utils.getViewProperty(mCustomVideoView);
        /**
         * 将desationview移到originalview位置处
         */
        deltaY = (mStartBundle.getInt(Utils.PROPNAME_SCREENLOCATION_TOP)
                - mEndBundle.getInt(Utils.PROPNAME_SCREENLOCATION_TOP));
        mCustomVideoView.setTranslationY(deltaY);
    }


    @Override
    public void onBackPressed() {
        onClickBackBtn();
    }

    /**
     * @createTime: 2018/10/29
     * @author lady_zhou
     * @Description 焦点状态改变时的回调
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mCustomVideoView.isShowFullBtn(false); //防止第一次，有些手机仍显示全屏按钮
        if (!hasFocus) {
            //未取到焦点时逻辑
            mPosition = mCustomVideoView.getCurrentPosition();
            mCustomVideoView.pauseForFullScreen();
        } else {
            //表明，我们的dialog是首次创建且首次获得焦点
            if (isFirst) { //为了适配某些手机不执行seekandresume中的播放方法
                mCustomVideoView.seekAndResume(mPosition);
            } else {
                //取得焦点时的逻辑
                mCustomVideoView.resume();//恢复视频播放
            }
        }
        isFirst = false;
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/29
     * @author lady_zhou
     * @Description dialog销毁时调用
     */
    @Override
    public void dismiss() {
        //从树中移走
        mParentView.removeView(mCustomVideoView);
        super.dismiss();
    }

    /**********************
     *  实现了ADVideoPlayerListener接口中的方法
     * *****************************/

    @Override
    public void onBufferUpdate(int time) {
        try {
            if (mVideoBean != null) {
                ReportManager.suReport(mVideoBean.middleMonitor, time / SDKConstant.MILLION_UNIT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickFullScreenBtn() {
        onClickVideo();
    }

    @Override
    public void onClickVideo() {
        String desationUrl = mVideoBean.clickUrl;
        if (mSlotListener != null) {
            if (mCustomVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
                mSlotListener.onClickVideo(desationUrl);
                try {
                    ReportManager.pauseVideoReport(mVideoBean.clickMonitor, mCustomVideoView.getCurrentPosition()
                            / SDKConstant.MILLION_UNIT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //走默认样式
            if (mCustomVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
                Intent intent = new Intent(mContext, AdBrowserActivity.class);
                intent.putExtra(AdBrowserActivity.KEY_URL, mVideoBean.clickUrl);
                mContext.startActivity(intent);
                try {
                    ReportManager.pauseVideoReport(mVideoBean.clickMonitor, mCustomVideoView.getCurrentPosition()
                            / SDKConstant.MILLION_UNIT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClickBackBtn() {
        //退出动画
        runExitAnimator();
    }

    @Override
    public void onClickPlay() {

    }

    public void setViewBundle(Bundle bundle) {
        mStartBundle = bundle;
    }

    public void setSlotListener(VideoSlot.AdSDKSlotListener slotListener) {
        this.mSlotListener = slotListener;
    }

    @Override
    public void onAdVideoLoadSuccess() {
        if (mCustomVideoView != null) {
            mCustomVideoView.resume();
        }
    }

    @Override
    public void onAdVideoLoadFailed() {

    }

    //与小屏播放时的处理不一样，单独处理
    @Override
    public void onAdVideoLoadComplete() {
        try {
            int position = mCustomVideoView.getDuration() / SDKConstant.MILLION_UNIT;
            ReportManager.sueReport(mVideoBean.endMonitor, true, position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dismiss();
        if (mListener != null) {
            mListener.playComplete();
        }
    }

    /**
     * @createTime: 2018/10/29
     * @author lady_zhou
     * @Description 注入事件监听类
     */
    public void setListener(FullToSmallListener listener) {
        this.mListener = listener;
    }

    /**
     * @author lady_zhou
     * @createTime: 2018/10/29
     * @Description 与我们的业务逻辑层(videoSlot)进行通信
     */
    public interface FullToSmallListener {
        void getCurrentPlayPosition(int position);//全屏播放中点击关闭按钮或者back键时回调

        void playComplete();//全屏播放结束时回调
    }
}
