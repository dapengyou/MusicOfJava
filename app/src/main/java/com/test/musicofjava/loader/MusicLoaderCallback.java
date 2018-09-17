package com.test.musicofjava.loader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.webkit.ValueCallback;

import com.test.baselibrary.bean.Music;

import java.util.List;

public class MusicLoaderCallback implements LoaderManager.LoaderCallbacks {
    private Context mContext;

    public MusicLoaderCallback(Context context, ValueCallback<List<Music>> callback) {
        this.mContext = context;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
