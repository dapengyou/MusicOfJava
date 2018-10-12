package com.test.musicofjava.api;

import android.app.Activity;

import com.test.baselibrary.BuildConfig;
import com.test.baselibrary.http.ApiCallback;
import com.test.baselibrary.http.ApiRequestManager;
import com.test.baselibrary.http.InterfaceParameters;
import com.test.baselibrary.http.RetrofitCallBack;
import com.test.musicofjava.bean.Address;

import retrofit2.Call;

/**
 * @createTime: 2018/10/10
 * @author: lady_zhou
 * @Description:
 */
public class ApiWebTest {
    //api
    private LineMusicApi mApi;

    public ApiWebTest() {
        mApi = ApiRequestManager.getInstance().create(LineMusicApi.class);
    }

    public void webTest(String name, String tag, int start, int count, Activity activity, ApiCallback<Address> callBack) {
        Call<Address> mCall = mApi.getString(name,tag,start,count);
        mCall.enqueue(new CallBack(callBack,activity,Address.class,RetrofitCallBack.REQUEST_ID_THREE));
    }
}
