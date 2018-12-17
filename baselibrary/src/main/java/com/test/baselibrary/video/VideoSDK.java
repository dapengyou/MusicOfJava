package com.test.baselibrary.video;

import android.view.ViewGroup;

import com.test.baselibrary.Utils.Utils;
import com.test.baselibrary.video.bean.VideoBean;
import com.test.baselibrary.video.report.ReportManager;
import com.test.baselibrary.video.slot.VideoSlot;
import com.test.baselibrary.video.util.ResponseEntityToModule;
import com.test.baselibrary.video.widget.CustomVideoView;

/**
 * @createTime: 2018/10/30
 * @author: lady_zhou
 * @Description: 封装API  与外界进行通信
 */
public class VideoSDK implements VideoSlot.AdSDKSlotListener {
    //the ad container
    private ViewGroup mParentView;

    private VideoSlot mAdSlot;
    private VideoBean mInstance = null;
    //the listener to the app layer
    private AdSDkInterface mListener;
    private CustomVideoView.ADFrameImageLoadListener mFrameLoadListener;

    public VideoSDK(ViewGroup parentView, String instance,
                          CustomVideoView.ADFrameImageLoadListener frameLoadListener) {
        this.mParentView = parentView;
        //将json字符串转换成实体对象
        this.mInstance = (VideoBean) ResponseEntityToModule.
                parseJsonToModule(instance, VideoBean.class);
        this.mFrameLoadListener = frameLoadListener;
        load();
    }
    public void load() {
        if (mInstance != null && mInstance.resource != null) {
            mAdSlot = new VideoSlot(mInstance, this, mFrameLoadListener);
            //发送解析成功事件
//            sendAnalizeReport(Params.ad_analize, HttpConstant.AD_DATA_SUCCESS);
        } else {
            mAdSlot = new VideoSlot(null, this, mFrameLoadListener); //创建空的slot,不响应任何事件
            if (mListener != null) {
                mListener.onAdFailed();
            }
//            sendAnalizeReport(Params.ad_analize, HttpConstant.AD_DATA_FAILED);
        }
    }
//    private void sendAnalizeReport(Params step, String result) {
//        try {
//            ReportManager.sendAdMonitor(Utils.isPad(mParentView.getContext().
//                            getApplicationContext()), mInstance == null ? "" : mInstance.resourceID,
//                    (mInstance == null ? null : mInstance.adid), Utils.getAppVersion(mParentView.getContext()
//                            .getApplicationContext()), step, result);
//        } catch (Exception e) {
//
//        }
//    }


    @Override
    public ViewGroup getAdParent() {
        return null;
    }

    @Override
    public void onAdVideoLoadSuccess() {

    }

    @Override
    public void onAdVideoLoadFailed() {

    }

    @Override
    public void onAdVideoLoadComplete() {

    }

    @Override
    public void onClickVideo(String url) {

    }
}
