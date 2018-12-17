package com.test.musicofjava.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.baselibrary.base.BaseFragment;
import com.test.baselibrary.http.ApiCallback;
import com.test.baselibrary.video.widget.CustomVideoView;
import com.test.musicofjava.MyBaseFragment;
import com.test.musicofjava.R;
import com.test.musicofjava.api.ApiWebTest;
import com.test.musicofjava.api.LineMusicApi;
import com.test.musicofjava.bean.Address;

/**
 * 线上音乐
 */
public class LineMusicFragment extends MyBaseFragment {
    private Button mBtTest;
    private ApiWebTest mApiWebTest;
    private CustomVideoView mCustomVideoView;
    private ViewGroup mViewGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_line_music;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBtTest = findViewById(R.id.bt_test);

//        mCustomVideoView = findViewById(R.id.video_view);
        mViewGroup = findViewById(R.id.layout);
    }

    @Override
    protected void initData(Bundle arguments, Bundle savedInstanceState) {
        mApiWebTest = new ApiWebTest();
        mCustomVideoView = new CustomVideoView(getActivity(), mViewGroup);

        mCustomVideoView.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        mViewGroup.addView(mCustomVideoView);
    }

    @Override
    protected void initListener() {
        mBtTest.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.bt_test:
                testWeb();
                break;
        }
    }

    /**
     * @return : void
     * @date 创建时间: 2018/10/10
     * @author lady_zhou
     * @Description 测试网络
     */
    private void testWeb() {
        Log.e("===", "testWeb: " + getActivity());
        mApiWebTest.webTest("小王子", "", 0, 3, getActivity(), new ApiCallback<Address>() {
            @Override
            public void onSuccess(Address response) {
                Log.e("===", "return:" + response.toString() + "成功");

            }

            @Override
            public void onError(String err_msg) {
                Log.e("===", "错误");
            }

            @Override
            public void onFailure() {
                Log.e("===", "失败");
            }
        });
    }
}
