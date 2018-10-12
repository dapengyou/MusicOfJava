package com.test.baselibrary.http;


import android.os.Build;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * http拦截器，用于添加header
 *
 * @param <T>
 */
public class BaseInterceptor<T> implements Interceptor {

    private Map<String, T> headers;

    public BaseInterceptor(Map<String, T> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request()
                .newBuilder();
        //添加请求头
//        builder.addHeader("X-Access-Auth-Token", AppConfig.getToken());
        builder.addHeader("X-REQUEST-SIDE", "APP");
        builder.addHeader("User-Agent", makeUA());

        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey) == null ? "" : (String) headers.get(headerKey)).build();
            }
        }
        return chain.proceed(builder.build());

    }

    /**
     * Android 获取手机设备信息
     * <p>
     * 生产厂商：android.os.Build.MANUFACTURER
     * 品牌：android.os.Build.BRAND
     * 型号：android.os.Build.MODEL
     * Android 版本：android.os.Build.VERSION.RELEASE
     * Android sdk：android.os.Build.VERSION.SDK_INT
     *
     * @return
     */
    private String makeUA() {
        return Build.BRAND + "/" + Build.MODEL + "/" + Build.VERSION.RELEASE;
    }
}
