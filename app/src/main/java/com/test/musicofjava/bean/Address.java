package com.test.musicofjava.bean;

import com.test.baselibrary.bean.response.BaseBean;

/**
 * @createTime: 2018/10/10
 * @author: lady_zhou
 * @Description:
 */
public class Address  extends BaseBean{
    private String name;
    private String Tag;
    private String start;
    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
