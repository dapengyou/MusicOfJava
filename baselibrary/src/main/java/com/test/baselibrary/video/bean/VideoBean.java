package com.test.baselibrary.video.bean;

import com.test.baselibrary.video.bean.emevent.EMEvent;

import java.util.ArrayList;

/**
 * @createTime: 2018/10/24
 * @author: lady_zhou
 * @Description: video节点bean
 */
public class VideoBean {
    public String resourceID;
    public String adid;
    public String resource;
    public String thumb;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public String type;
    public EMEvent event;
}
