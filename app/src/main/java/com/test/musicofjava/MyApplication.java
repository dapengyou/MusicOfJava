package com.test.musicofjava;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.test.baselibrary.module.ActivityManager;

public class MyApplication extends Application {
    private static MyApplication sInstance = null;

    public static MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        AppCache.getInstance().init(this);
        this.registerActivityLifecycleCallbacks(new OverallActivityLifecycleCallbacks());

    }
   /**
    * @createTime: 2018/10/12
    * @author  lady_zhou
    * @Description   应用内所有Activity生命周期回调，可以在里面添加统一的界面生命周期统计
    */
    private class OverallActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            ActivityManager.getInstance().addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            ActivityManager.getInstance().removeActivity(activity);
        }

    }
}
