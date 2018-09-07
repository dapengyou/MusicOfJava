package com.test.musicofjava.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.baselibrary.base.BaseActivity;
import com.test.musicofjava.R;
import com.test.musicofjava.adapter.FragmentAdapter;
import com.test.musicofjava.fragment.LineMusicFragment;
import com.test.musicofjava.fragment.LocalMusicFragment;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ImageView mIvSeach;//搜索
    private ImageView mIvMenu;//抽屉菜单
    private TextView mTvLocalMusic;//本地音乐
    private TextView mTvLineMusic;//线上音乐

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private View mNavigationHeader;

    //本地音乐fragment切换
    private LocalMusicFragment mLocalMusicFragment;
    private LineMusicFragment mLineMusicFragment;
    private ViewPager mViewPager;

    private FrameLayout mFlPlayBar;//音乐播放控制条
    private boolean isShowPlayFragment;//用于判断是否展示了播放页面


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mNavigationView = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mIvMenu = findViewById(R.id.iv_menu);
        mIvSeach = findViewById(R.id.iv_search);
        mTvLineMusic = findViewById(R.id.tv_online_music);
        mTvLocalMusic = findViewById(R.id.tv_local_music);

        mViewPager = findViewById(R.id.viewpager);

        mFlPlayBar = findViewById(R.id.fl_play_bar);

        setView();
    }

    private void setView() {
        //添加navigation 的头view
        mNavigationHeader = LayoutInflater.from(this).inflate(R.layout.navigation_header, mNavigationView, false);
        mNavigationView.addHeaderView(mNavigationHeader);

        setPager();
    }

    /**
     * 设置fragment切换pager
     */
    private void setPager() {
        mLineMusicFragment = new LineMusicFragment();
        mLocalMusicFragment = new LocalMusicFragment();

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(mLocalMusicFragment);
        fragmentAdapter.addFragment(mLineMusicFragment);
        mViewPager.setAdapter(fragmentAdapter);
        mTvLocalMusic.setSelected(true);

    }

    @Override
    protected void initData(Intent intent, Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {
        mIvSeach.setOnClickListener(this);
        mIvMenu.setOnClickListener(this);
        mTvLineMusic.setOnClickListener(this);
        mTvLocalMusic.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(this);

        mFlPlayBar.setOnClickListener(this);

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.tv_online_music:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tv_local_music:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.fl_play_bar:
                showPlayActivity();
                break;
        }
    }

    /**
     * 展示播放页面
     */
    private void showPlayActivity() {
        if (isShowPlayFragment) {
            return;
        }

        startActivity(new Intent(this, PlayActivity.class));
        this.overridePendingTransition(R.anim.anim_bottom_in, 0);

    }

    @Override
    public void onBackPressed() {
        //判断DrawerLayout 是否被抽出
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }

        super.onBackPressed();
    }

    //viewpager 监听事件的三个方法重写
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            mTvLocalMusic.setSelected(true);
            mTvLineMusic.setSelected(false);
        } else {
            mTvLocalMusic.setSelected(false);
            mTvLineMusic.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
