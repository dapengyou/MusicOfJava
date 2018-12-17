package com.test.baselibrary.video.slot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.test.baselibrary.Utils.Utils;
import com.test.baselibrary.video.AdParameters;
import com.test.baselibrary.video.activity.AdBrowserActivity;
import com.test.baselibrary.video.bean.VideoBean;
import com.test.baselibrary.video.constant.SDKConstant;
import com.test.baselibrary.video.report.ReportManager;
import com.test.baselibrary.video.widget.CustomVideoView;
import com.test.baselibrary.video.widget.VideoFullDialog;

/**
 * @createTime: 2018/10/24
 * @author: lady_zhou
 * @Description: video业务逻辑层
 */
public class VideoSlot implements CustomVideoView.ADVideoPlayerListener {

    private Context mContext;

    /**
     * UI
     */
    private CustomVideoView mCustomVideoView;
    private ViewGroup mParentView;//要添加到父容器中

    /**
     * Data
     */
    private VideoBean mVideoBean;
    private AdSDKSlotListener mSlotListener;
    private boolean canPause = false;//是否可自动暂停标志位
    private int lastArea = 0;//防止将要滑入滑出时播放器的状态改变

    public VideoSlot(VideoBean adInstance, AdSDKSlotListener slotLitener, CustomVideoView.ADFrameImageLoadListener frameLoadListener) {
        mVideoBean = adInstance;
        mSlotListener = slotLitener;
        mParentView = slotLitener.getAdParent();
        mContext = mParentView.getContext();
        initVideoView(frameLoadListener);
    }

    //初始化
    private void initVideoView(CustomVideoView.ADFrameImageLoadListener frameLoadListener) {
        mCustomVideoView = new CustomVideoView(mContext, mParentView);

        if (mVideoBean != null) {
            mCustomVideoView.setDataSource(mVideoBean.resource);
            mCustomVideoView.setFrameURI(mVideoBean.thumb);
            mCustomVideoView.setFrameLoadListener(frameLoadListener);
            mCustomVideoView.setListener(this);
        }

        //小屏到大屏切换
        RelativeLayout paddingView = new RelativeLayout(mContext);
        paddingView.setBackgroundColor(mContext.getResources().getColor(android.R.color.black));
        paddingView.setLayoutParams(mCustomVideoView.getLayoutParams());
        mParentView.addView(paddingView);
        mParentView.addView(mCustomVideoView);
    }


    /**
     * @return : boolean
     * @date 创建时间: 2018/10/30
     * @author lady_zhou
     * @Description 是否播放  用于第二步
     */
    private boolean isPlaying() {
        if (mCustomVideoView != null) {
            return mCustomVideoView.isPlaying();
        }
        return false;
    }

    /**
     * @return : boolean
     * @date 创建时间: 2018/10/30
     * @author lady_zhou
     * @Description 是否真正的暂停  用于第二步
     */
    private boolean isRealPause() {
        if (mCustomVideoView != null) {
            return mCustomVideoView.isRealPause();
        }
        return false;
    }

    /**
     * @return : boolean
     * @date 创建时间: 2018/10/30
     * @author lady_zhou
     * @Description 是否播放完成  用于第二步
     */
    private boolean isComplete() {
        if (mCustomVideoView != null) {
            return mCustomVideoView.isComplete();
        }
        return false;
    }

    //pause the  video  用于第二步
    private void pauseVideo(boolean isAuto) {
        if (mCustomVideoView != null) {
            if (isAuto) {
                //发自动暂停监测
                if (!isRealPause() && isPlaying()) {
                    try {
                        ReportManager.pauseVideoReport(mVideoBean.event.pause.content, getPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mCustomVideoView.seekAndPause(0);
        }
    }

    //resume the video 用于第二步
    private void resumeVideo() {
        if (mCustomVideoView != null) {
            mCustomVideoView.resume();
            if (isPlaying()) {
                sendSUSReport(true); //发自动播放监测
            }
        }
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/30
     * @author lady_zhou
     * @Description 实现自动播放和暂停功能   属于第二步
     */
    public void updateAdInScrollView() {
        int currentArea = Utils.getVisiblePercent(mParentView);
        //小于0表示未出现在屏幕上，不做任何处理
        if (currentArea <= 0) {
            return;
        }
        //刚要滑入和滑出时，异常状态的处理
        if (Math.abs(currentArea - lastArea) >= 100) {
            return;
        }
        if (currentArea < SDKConstant.VIDEO_SCREEN_PERCENT) {
            //进入自动暂停状态
            if (canPause) {
                pauseVideo(true);
                canPause = false;
            }
            lastArea = 0;
            mCustomVideoView.setIsComplete(false); // 滑动出50%后标记为从头开始播
            mCustomVideoView.setIsRealPause(false); //以前叫setPauseButtonClick()
            return;
        }

        if (isRealPause() || isComplete()) {
            //进入手动暂停或者播放结束，播放结束和不满足自动播放条件都作为手动暂停
            pauseVideo(false);
            canPause = false;
            return;
        }

        //满足自动播放条件或者用户主动点击播放，开始播放
        if (Utils.canAutoPlay(mContext, AdParameters.getCurrentSetting())
                || isPlaying()) {
            lastArea = currentArea;
            resumeVideo();
            canPause = true;
            mCustomVideoView.setIsRealPause(false);
        } else {
            pauseVideo(false);
            mCustomVideoView.setIsRealPause(true); //不能自动播放则设置为手动暂停效果
        }
    }

    public void destroy() {
        mCustomVideoView.destroy();
        mCustomVideoView = null;
        mContext = null;
        mVideoBean = null;
    }

    /*
     *     ADVideoPlayerListener接口实现   属于第一步
     */
    @Override
    public void onBufferUpdate(int time) {
        try {
            ReportManager.suReport(mVideoBean.middleMonitor, time / SDKConstant.MILLION_UNIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/30
     * @author lady_zhou
     * @Description 实现对播放模式的切换  属于第三步
     */
    @Override
    public void onClickFullScreenBtn() {
        try {
            ReportManager.fullScreenReport(mVideoBean.event.full.content, getPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取videoview在当前界面的属性
        Bundle bundle = Utils.getViewProperty(mParentView);
        //将播放器从view树中移除
        mParentView.removeView(mCustomVideoView);

        VideoFullDialog dialog = new VideoFullDialog(mContext, mCustomVideoView, mVideoBean,
                mCustomVideoView.getCurrentPosition());
        dialog.setListener(new VideoFullDialog.FullToSmallListener() {
            @Override
            public void getCurrentPlayPosition(int position) {
                //在全屏视频播放的时候点击了返回
                backToSmallMode(position);
            }

            @Override
            public void playComplete() {
                //全屏播放完成以后的事件回调
                bigPlayComplete();
            }
        });
        dialog.setViewBundle(bundle); //为Dialog设置播放器数据Bundle对象
        dialog.setSlotListener(mSlotListener);
        dialog.show();
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/30
     * @author lady_zhou
     * @Description 全屏播放完成以后的事件回调  用于第三步
     */
    private void bigPlayComplete() {
        if (mCustomVideoView.getParent() == null) {
            mParentView.addView(mCustomVideoView);
        }
        mCustomVideoView.setTranslationY(0); //防止动画导致偏离父容器
        mCustomVideoView.isShowFullBtn(true);
        mCustomVideoView.mute(true);
        mCustomVideoView.setListener(this);
        mCustomVideoView.seekAndPause(0);//跳到视频流的开始位置，并置为暂停状态
        canPause = false;
    }

    /**
     * @param position :
     * @return : void
     * @date 创建时间: 2018/10/29
     * @author lady_zhou
     * @Description 返回小屏的时候  用于第三步
     */
    private void backToSmallMode(int position) {
        if (mCustomVideoView.getParent() == null) {
            mParentView.addView(mCustomVideoView);
        }
        mCustomVideoView.setTranslationY(0); //防止动画导致偏离父容器
        mCustomVideoView.isShowFullBtn(true);//显示我们的全屏按钮
        mCustomVideoView.mute(true);//小屏静音播放
        mCustomVideoView.setListener(this);//重新设置监听为我们的业务逻辑层
        mCustomVideoView.seekAndResume(position);//是播放器跳到指定位置并且播放
        canPause = true; // 标为可自动暂停
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

    }

    @Override
    public void onClickPlay() {
        sendSUSReport(false);
    }

    @Override
    public void onAdVideoLoadSuccess() {
        if (mSlotListener != null) {
            mSlotListener.onAdVideoLoadSuccess();
        }
    }

    @Override
    public void onAdVideoLoadFailed() {
        if (mSlotListener != null) {
            mSlotListener.onAdVideoLoadFailed();
        }

        //加载失败全部回到初始状态
        canPause = false;
    }

    @Override
    public void onAdVideoLoadComplete() {
        // TODO: 2018/10/25 使用网络通知服务器，，等网络写好再说 
        try {
            ReportManager.sueReport(mVideoBean.endMonitor, false, getDuration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mSlotListener != null) {
            mSlotListener.onAdVideoLoadComplete();
        }
        mCustomVideoView.setIsRealPause(true);
    }


    /**
     * @return : int
     * @date 创建时间: 2018/10/25
     * @author lady_zhou
     * @Description 获取视频总共有多长时间  用于第一步
     */
    private int getDuration() {
        return mCustomVideoView.getDuration() / SDKConstant.MILLION_UNIT;
    }

    /**
     * @return : int
     * @date 创建时间: 2018/10/25
     * @author lady_zhou
     * @Description 获取播放器当前秒数  用于第二步
     */
    private int getPosition() {
        return mCustomVideoView.getCurrentPosition() / SDKConstant.MILLION_UNIT;
    }

    /**
     * @param isAuto :
     * @return : void
     * @date 创建时间: 2018/10/30
     * @author lady_zhou
     * @Description 发送视频开始播放监测 用于第二步
     */
    private void sendSUSReport(boolean isAuto) {
        try {
            ReportManager.susReport(mVideoBean.startMonitor, isAuto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //传递消息到appcontext层
    public interface AdSDKSlotListener {

        ViewGroup getAdParent();

        void onAdVideoLoadSuccess();

        void onAdVideoLoadFailed();

        void onAdVideoLoadComplete();

        void onClickVideo(String url);
    }
}
