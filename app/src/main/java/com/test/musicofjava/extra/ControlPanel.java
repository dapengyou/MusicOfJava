package com.test.musicofjava.extra;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.baselibrary.bean.Music;
import com.test.musicofjava.R;
import com.test.musicofjava.activity.MusicListActivity;
import com.test.musicofjava.service.AudioPlayer;
import com.test.musicofjava.service.OnPlayerEventListener;

/**
 * Created by hzwangchenyan on 2018/1/26.
 */
public class ControlPanel implements View.OnClickListener, OnPlayerEventListener {
    private ImageView mIvCover;
    private TextView mTvTitle;
    private TextView mTvArtist;
    private ImageView mIvPlay;
    private ImageView mIvPlaylist;
    private ProgressBar mProgressBar;

    private View mView;
    private Context mContext;

    public ControlPanel(Context context, View view) {
        this.mContext = context;
        this.mView = view;
        initView();
        initListener();

        onChangeMusic(AudioPlayer.getInstance().getPlayMusic());
    }


    private void initView() {
        mProgressBar = mView.findViewById(R.id.pb_play_bar);
        mIvPlaylist = mView.findViewById(R.id.iv_playlist);
        mIvPlay = mView.findViewById(R.id.iv_play);
        mIvCover = mView.findViewById(R.id.iv_cover);
        mTvArtist = mView.findViewById(R.id.tv_artist);
        mTvTitle = mView.findViewById(R.id.tv_title);
    }

    private void initListener() {
        mIvPlay.setOnClickListener(this);
        mIvPlaylist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                AudioPlayer.getInstance().playPause();
                break;
            case R.id.iv_playlist:
                Context context = mIvPlaylist.getContext();
                Intent intent = new Intent(context, MusicListActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    @Override
    public void onChangeMusic(Music music) {
        if (music == null) {
            return;
        }
//        Bitmap cover = CoverLoader.get().loadThumb(music);
//        mIvCover.setImageBitmap(cover);
        mTvTitle.setText(music.getTitle());
        mTvArtist.setText(music.getArtist());
        mIvPlay.setSelected(AudioPlayer.getInstance().isPlaying() || AudioPlayer.getInstance().isPreparing());
        mProgressBar.setMax((int) music.getDuration());
        mProgressBar.setProgress((int) AudioPlayer.getInstance().getAudioPosition());
    }

    @Override
    public void onPlayerStart() {
        mIvPlay.setSelected(true);
    }

    @Override
    public void onPlayerPause() {
        mIvPlay.setSelected(false);
    }

    @Override
    public void onPublish(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void onBufferingUpdate(int percent) {
    }
}
