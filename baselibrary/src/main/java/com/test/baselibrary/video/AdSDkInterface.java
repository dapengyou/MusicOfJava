package com.test.baselibrary.video;

/**
 * @createTime: 2018/10/30
 * @author: lady_zhou
 * @Description: 最终通知应用层广告是否成功
 */
public interface AdSDkInterface {
    void onAdSuccess();

    void onAdFailed();

    void onClickVideo(String url);
}
