package com.test.baselibrary.bean.response;

import java.io.Serializable;

/**
 * Created by lady_zhou on 2018/4/9.
 */

public class BaseBean implements Serializable {
    private long id;//主键
    private String imageUrl;//图片

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
