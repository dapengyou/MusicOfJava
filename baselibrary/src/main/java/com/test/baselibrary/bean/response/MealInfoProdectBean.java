package com.test.baselibrary.bean.response;

import java.io.Serializable;

/**
 * Created by lady_zhou on 2018/4/13.
 */

public class MealInfoProdectBean implements Serializable {
    private String title;//标题
    private String content;//内容

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
