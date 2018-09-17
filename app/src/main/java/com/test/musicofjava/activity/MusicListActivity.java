package com.test.musicofjava.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.baselibrary.Utils.ToastUtil;
import com.test.baselibrary.bean.Music;
import com.test.musicofjava.AppCache;
import com.test.musicofjava.MyBaseActivity;
import com.test.musicofjava.R;
import com.test.musicofjava.adapter.LocalMusicAdapter;
import com.test.musicofjava.service.AudioPlayer;
import com.test.musicofjava.service.OnPlayerEventListener;

public class MusicListActivity extends MyBaseActivity implements OnPlayerEventListener {
    private RecyclerView mRecyclerView;
    private LocalMusicAdapter mLocalMusicAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.rv_content);

    }

    @Override
    protected void initListener() {
        AudioPlayer.getInstance().addOnPlayEventListener(this);
    }

    @Override
    protected void initData(Intent intent, Bundle savedInstanceState) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mLocalMusicAdapter = new LocalMusicAdapter(AudioPlayer.getInstance().getMusicList());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mLocalMusicAdapter);

        mLocalMusicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AudioPlayer.getInstance().play(position);
            }
        });
        mLocalMusicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_more:
                        dialog(position);
                        break;
                }
            }
        });
    }

    public void dialog(final int position) {
        String[] items = new String[]{"移除"};
        Music music = AudioPlayer.getInstance().getMusicList().get(position);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(music.getTitle());
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AudioPlayer.getInstance().delete(position);
                mLocalMusicAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    @Override
    public void onChangeMusic(Music music) {
        mLocalMusicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayerStart() {

    }

    @Override
    public void onPlayerPause() {

    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    protected void onDestroy() {
        AudioPlayer.getInstance().removeOnPlayEventListener(this);
        super.onDestroy();
    }
}

