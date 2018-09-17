package com.test.musicofjava;

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
}
