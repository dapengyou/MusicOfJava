package com.test.baselibrary.bean.response;

/**
 * Created by lady_zhou on 2018/4/9.
 */

public class FirstCommodityBean extends BaseBean {
    private String title;//标题
    private String sex;//性别
    private double price;//价格
    private int peopleNumber;//人数
    private String type;//类型
    private boolean isHaveSex;

    private boolean isShow;//是否展示类型

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isHaveSex() {
        return isHaveSex;
    }

    public void setHaveSex(boolean haveSex) {
        isHaveSex = haveSex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
