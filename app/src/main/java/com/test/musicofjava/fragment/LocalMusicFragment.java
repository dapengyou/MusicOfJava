package com.test.musicofjava.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.baselibrary.Utils.MusicUtils;
import com.test.baselibrary.Utils.PermissionUtils;
import com.test.baselibrary.Utils.ToastUtil;
import com.test.baselibrary.base.BaseFragment;
import com.test.baselibrary.bean.Music;
import com.test.musicofjava.AppCache;
import com.test.musicofjava.MyBaseActivity;
import com.test.musicofjava.MyBaseFragment;
import com.test.musicofjava.R;
import com.test.musicofjava.adapter.LocalMusicAdapter;
import com.test.musicofjava.service.AudioPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地音乐
 */
public class LocalMusicFragment extends MyBaseFragment {
    private List<Music> mMusicList = null;
    private RecyclerView mRecyclerView;
    private LocalMusicAdapter mLocalMusicAdapter;
    private Music music;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_music;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.rv_content);
    }

    @Override
    protected void initData(Bundle arguments, Bundle savedInstanceState) {
        requestData();
        initRecyclerView();
    }

    /**
     * 获取本地歌曲数据
     */
    private void requestData() {
        PermissionUtils.with(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .result(new PermissionUtils.Result() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onGranted() {
                        show("233333");
                        new AsyncTask<Void, Void, List<Music>>() {
                            @Override
                            protected List<Music> doInBackground(Void... params) {
                                return MusicUtils.scanMusic(getContext());
                            }

                            @Override
                            protected void onPostExecute(List<Music> musicList) {
//                                mView.getRecentlyPlayedSongSuccess(musicList);
                                mMusicList = musicList;
                                mLocalMusicAdapter.setNewData(musicList);
                                AppCache.getInstance().getLocalMusicList().clear();
                                AppCache.getInstance().getLocalMusicList().addAll(musicList);


                            }
                        }.execute();
                    }

                    @Override
                    public void onDenied() {
                        show("没有访问权限");
                    }
                })
                .request();
    }

    private void initRecyclerView() {
        if (AppCache.getInstance().getLocalMusicList().size() > 0) {
            mLocalMusicAdapter = new LocalMusicAdapter(AppCache.getInstance().getLocalMusicList());
        } else {
            mLocalMusicAdapter = new LocalMusicAdapter(new ArrayList<Music>());
        }
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mLocalMusicAdapter);

        mLocalMusicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                music = AppCache.getInstance().getLocalMusicList().get(position);
                AudioPlayer.getInstance().addAndPlay(music);
                ToastUtil.showToast("已添加到播放列表");
            }
        });
    }
}
