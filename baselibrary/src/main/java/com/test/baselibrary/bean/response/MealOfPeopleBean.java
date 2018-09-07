package com.test.baselibrary.bean.response;

import java.io.Serializable;

/**
 * Created by lady_zhou on 2018/4/16.
 */

public class MealOfPeopleBean implements Serializable {
    private String imageUrl;
    private String title;
    private String introduction;

    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
