package com.test.musicofjava.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.baselibrary.Utils.GlideUtil;
import com.test.baselibrary.bean.Music;
import com.test.musicofjava.R;

import java.util.List;

public class LocalMusicAdapter extends BaseQuickAdapter<Music, BaseViewHolder> {


    public LocalMusicAdapter(List<Music> data) {
        super(R.layout.item_holder_music, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Music item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_artist, item.getArtist());

        ImageView imageView = helper.itemView.findViewById(R.id.iv_cover);
        if (!TextUtils.isEmpty(item.getCoverPath())) {
            GlideUtil.loadImage(mContext, item.getCoverPath(), imageView);
        }
        //子控件点击事件
        helper.addOnClickListener(R.id.iv_more);
    }
}
