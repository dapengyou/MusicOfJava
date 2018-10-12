package com.test.baselibrary.http.httpResponse;

import java.io.Serializable;

/**
 * @createTime: 2018/10/10
 * @author: lady_zhou
 * @Description: 网络请求返回对象公共抽象类
 */
public class BaseResponse implements Serializable {
    public String  data;
    public String errorMsg;
    public boolean isSuccess;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
