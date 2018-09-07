package com.test.musicofjava.dataSave;

import android.content.Context;
import android.content.SharedPreferences;

import com.test.musicofjava.MyApplication;

public class SharedPreferencesManager {
    private static final String SHARE_PREFERENCE_NAME = "sharedPreference.pre";//文件名
    private static final String PLAY_MODE = "play_mode";
    private static SharedPreferencesManager sInstance;
    private static SharedPreferences sSharedPreferences;
    private static SharedPreferences.Editor sEditor;

    public static SharedPreferencesManager getInstance() {
        if (sInstance == null) {
            synchronized (SharedPreferencesManager.class) {
                if (sInstance == null) {
                    sInstance = new SharedPreferencesManager();
                }
            }
        }
        return sInstance;
    }

    private SharedPreferencesManager() {
        sSharedPreferences = MyApplication.getInstance().getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sEditor = sSharedPreferences.edit();
    }

    /**
     * 获得播放模式
     *
     * @return
     */
    public int getPlayMode() {
        return getInt(PLAY_MODE, 0);
    }

    /**
     * 存储播放模式
     *
     * @param value 播放模式数值
     */
    public void savePlayMode(int value) {
        setInt(PLAY_MODE, value);
    }

    private void setInt(String key, int value) {
        sEditor.putInt(key, value);
        sEditor.commit();
    }

    private int getInt(String key, int defaultValue) {
        return sSharedPreferences.getInt(key, defaultValue);
    }

    private void setString(String key, String value) {
        sEditor.putString(key, value);
        sEditor.commit();
    }

    private String getInt(String key, String defaultValue) {
        return sSharedPreferences.getString(key, defaultValue);
    }

}
