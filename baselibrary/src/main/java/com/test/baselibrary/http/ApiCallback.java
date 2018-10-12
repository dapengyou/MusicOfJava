package com.test.baselibrary.http;

/**
 * api 回调
 *
 * @param <T>
 */
public interface ApiCallback<T> {
    // 请求数据成功
    void onSuccess(T response);

    // 请求数据错误
    void onError(String err_msg);

    // 网络请求失败
    void onFailure();
}
