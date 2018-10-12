package com.test.baselibrary.module;

import android.app.Activity;

import java.util.Stack;

/**
 * @author lady_zhou
 * @createTime: 2018/10/10
 * @Description Activity管理类
 */
public class ActivityManager {
    // 单例
    private static ActivityManager instance;
    // Activity堆栈
    private static Stack<Activity> activityStack;

    /**
     * @date 创建时间: 2018/10/10
     * @author lady_zhou
     * @Description 构造函数
     */
    private ActivityManager() {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
    }

    /**
     * @return : com.test.baselibrary.module.AppManager
     * @date 创建时间: 2018/10/10
     * @author lady_zhou
     * @Description 单例
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * @param activity :
     * @return : void
     * @date 创建时间: 2018/10/12
     * @author lady_zhou
     * @Description 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activity != null) {
            activityStack.add(activity);
        }
    }
    /**
     * @date 创建时间: 2018/10/12
     * @author  lady_zhou
     * @Description  从栈中移除指定的Activity
     * @param activity :
     * @return : void
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.removeElement(activity);
        }
    }

    /**
     * @return : android.app.Activity
     * @date 创建时间: 2018/10/10
     * @author lady_zhou
     * @Description 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = null;
        //栈里没有Activity
        if (activityStack == null) {
            return activity;

        }
        try {
            if (activityStack.size() > 0) {
                activity = activityStack.lastElement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return activity;

    }

    /**
     * @param activity : 当前Activity
     * @return : boolean
     * @date 创建时间: 2018/10/10
     * @author lady_zhou
     * @Description 判断某一个activity是否为当前activity
     */
    public boolean isCurrent(Activity activity) {
        if (activity == null || currentActivity() == null) {
            return false;
        }
        if (activity == currentActivity()) {
            return true;
        } else {
            return false;
        }
    }
}
