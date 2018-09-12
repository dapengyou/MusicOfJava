package com.test.musicofjava.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.test.baselibrary.Utils.BitmapUtil;
import com.test.baselibrary.Utils.Utils;
import com.test.musicofjava.R;

public class AlbumCoverView extends View implements ValueAnimator.AnimatorUpdateListener {
    private static final float POINTER_PLAY = 0.0f;
    private static final float POINTER_PAUSE = -25.0f;
    private static final float DISC_ROTATION_INCREASE = 0.5f;//旋转角度
    private static final long TIME_UPDATE = 50L;//更新时间

    private Context mContext;

    private Bitmap mFilmBitmap;//胶片图片
    private Bitmap mPointerBitmap;//指针图片

    private Drawable mTopLine;//顶部虚线
    private Drawable mCoverBorder;//外部半圆

    private Matrix mFilmMatrix = new Matrix();
    private Matrix mCoverMatrix = new Matrix();
    private Matrix mPointerMatrix = new Matrix();

    private int mTopLineHeight;
    private int mCoverBorderWidth;

    private ValueAnimator mPlayAnimator;
    private ValueAnimator mPauseAnimator;

    //旋转角度
    private float mPointerRotation = POINTER_PLAY;
    private float mFilmRotation = 0.0f;

    // 图片起始坐标
    private Point mFilmPoint = new Point();
    private Point mCoverPoint = new Point();
    private Point mPointerPoint = new Point();

    // 旋转中心坐标
    private Point mFilmCenterPoint = new Point();
    private Point mCoverCenterPoint = new Point();
    private Point mPointerCenterPoint = new Point();

    //是否播放
    private boolean isPlaying = false;
    public Handler mHandler = new Handler();

    public AlbumCoverView(Context context) {
        super(context);
        this.mContext = context;
        initData();
    }

    public AlbumCoverView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initData();

    }

    public AlbumCoverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initData();

    }

    /**
     * 确定子View布局位置
     *
     * @param changed
     * @param left    View左侧距父View左侧的距离
     * @param top     View顶部距父View顶部的距离
     * @param right   View右侧距父View左侧的距离
     * @param bottom  View底部距父View顶部的距离
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            initOnLayout();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {

        mCoverBorder = ContextCompat.getDrawable(mContext, R.drawable.play_page_cover_border_shape);
        mCoverBorderWidth = Utils.dp2px(mContext, 1);

        mFilmBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.play_page_disc);
        mPointerBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.play_page_needle);

        mPlayAnimator = ValueAnimator.ofFloat(POINTER_PAUSE, POINTER_PLAY);
        mPlayAnimator.setDuration(300);
        mPlayAnimator.addUpdateListener(this);

        mPauseAnimator = ValueAnimator.ofFloat(POINTER_PLAY, POINTER_PAUSE);
        mPlayAnimator.setDuration(300);
        mPlayAnimator.addUpdateListener(this);

    }

    /**
     * 动画监听
     *
     * @param animation
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //得到当前值
        mPointerRotation = (float) animation.getAnimatedValue();
        invalidate();
    }

    /**
     * 确定子view的坐标
     */
    private void initOnLayout() {
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        int unit = Math.min(getWidth(), getHeight()) / 8;

        mFilmBitmap = BitmapUtil.resizeImage(mFilmBitmap, unit * 6, unit * 6);
        mPointerBitmap = BitmapUtil.resizeImage(mPointerBitmap, unit * 2, unit * 3);

        int filmOffsetY = mPointerBitmap.getHeight() / 2;

        mFilmPoint.x = (getWidth() - mFilmBitmap.getWidth()) / 2;
        mFilmPoint.y = filmOffsetY;

        mPointerPoint.x = getWidth() / 2 - mPointerBitmap.getWidth() / 6;
        mPointerPoint.y = -mPointerBitmap.getWidth() / 6;

        mFilmCenterPoint.x = getWidth() / 2;
        mFilmCenterPoint.y = mFilmBitmap.getHeight() / 2 + filmOffsetY;

        mCoverCenterPoint.x = mFilmCenterPoint.x;
        mCoverCenterPoint.y = mFilmCenterPoint.y;

        mPointerCenterPoint.x = mFilmCenterPoint.x;
        mPointerCenterPoint.y = 0;
    }

    /**
     * 绘制
     *
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 2.绘制黑胶唱片外侧半透明边框
        // 指的是drawable将在被绘制在canvas的哪个矩形区域内
        mCoverBorder.setBounds(mFilmPoint.x - mCoverBorderWidth,
                mFilmPoint.y - mCoverBorderWidth,
                mFilmPoint.x + mFilmBitmap.getWidth() + mCoverBorderWidth,
                mFilmPoint.y + mFilmBitmap.getHeight() + mCoverBorderWidth);
        mCoverBorder.draw(canvas);//画到画布上

        // 3.绘制黑胶
        // 设置旋转中心和旋转角度，setRotate和preTranslate顺序很重要
        mFilmMatrix.setRotate(mFilmRotation, mFilmCenterPoint.x, mFilmCenterPoint.y);
        // 设置图片起始坐标
        mFilmMatrix.preTranslate(mFilmPoint.x, mFilmPoint.y);
        canvas.drawBitmap(mFilmBitmap, mFilmMatrix, null);

        // 5.绘制指针
        mPointerMatrix.setRotate(mPointerRotation, mPointerCenterPoint.x, mPointerCenterPoint.y);
        mPointerMatrix.preTranslate(mPointerPoint.x, mPointerPoint.y);
        canvas.drawBitmap(mPointerBitmap, mPointerMatrix, null);
    }

    /**
     * 音乐开始后动画
     */
    public void start() {
        if (isPlaying) {
            return;
        }
        isPlaying = true;
        mHandler.post(mRotationRunnable);
        mPlayAnimator.start();
    }

    /**
     * 音乐暂停后动画
     */
    public void pause() {
        if (!isPlaying) {
            return;
        }
        isPlaying = false;
        mHandler.removeCallbacks(mRotationRunnable);
        mPauseAnimator.start();
    }

    private Runnable mRotationRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying) {
                mFilmRotation += DISC_ROTATION_INCREASE;
                if (mFilmRotation >= 360) {
                    mFilmRotation = 0;
                }
                invalidate();
            }
            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };

    public void initNeedle(boolean isPlaying) {
        mPointerRotation = isPlaying ? POINTER_PLAY : POINTER_PAUSE;
        invalidate();
    }
}
