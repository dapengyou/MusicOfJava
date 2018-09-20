package com.test.musicofjava;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.test.baselibrary.Utils.ToastUtil;
import com.test.baselibrary.bean.Music;

import java.util.ArrayList;
import java.util.List;

public class AppCache {
    private Context mContext;
    private final List<Music> mLocalMusicList = new ArrayList<>();
    private static AppCache mInstance;
    private final List<Activity> mActivityStack = new ArrayList<>();


    private AppCache() {

    }

    public static AppCache getInstance() {
        if (mInstance == null) {
            mInstance = new AppCache();
        }
        return mInstance;
    }

    public void init(Application application) {
        mContext = application.getApplicationContext();
        ToastUtil.init(mContext);
    }

    public List<Music> getLocalMusicList() {
        return mLocalMusicList;
    }

    /**
     * 清空活动栈
     */
    public void clearStack() {
        List<Activity> activityStack = mActivityStack;
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityStack.clear();
    }
}
