package com.test.musicofjava.api;

import android.app.Activity;

import com.test.baselibrary.http.ApiCallback;
import com.test.baselibrary.http.RetrofitCallBack;

/**
 * @createTime: 2018/10/10
 * @author: lady_zhou
 * @Description:
 */
public class CallBack extends RetrofitCallBack {
    public CallBack(ApiCallback mCallback, Activity activity, Class typeCls, String requestId) {
        super(mCallback, activity, typeCls, requestId);
    }

    @Override
    public void compatibleData() {

    }
}
