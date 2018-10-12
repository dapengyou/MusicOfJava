package com.test.baselibrary.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * retrofit api 请求
 */
public class ApiRequestManager {
    private Retrofit mRetrofit;

    private static ApiRequestManager instance;

    /**
     * 构造方法
     */
    public ApiRequestManager() {
//        OkHttpClient client = new OkHttpClient.Builder()
//                //添加okhttp拦截器
//                .addInterceptor(new OkHttpInterceptor())
//                .build();
//        mRetrofit = new Retrofit.Builder()
//                .baseUrl(InterfaceParameters.REQUEST_HTTP_URL)
//                //增加返回值为Gson的支持
//                .addConverterFactory(ApiConvertFactory.create())
//                .client(client)
//                .build();
    }

    /**
     * 单例
     *
     * @return
     */
    public static  ApiRequestManager getInstance() {
        if (instance == null) {
            instance = new ApiRequestManager();
        }
        return instance;
    }

    /**
     * 创建api接口
     *
     * @param service
     * @return
     */
    public <T> T create(Class<T> service) {
        OkHttpClient client = new OkHttpClient.Builder()
                //添加okhttp拦截器
                .addInterceptor(new OkHttpInterceptor())
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(InterfaceParameters.REQUEST_HTTP_URL)
                //增加返回值为Gson的支持
                .addConverterFactory(ApiConvertFactory.create())
                .client(client)
                .build();
        return mRetrofit.create(service);
    }
}
