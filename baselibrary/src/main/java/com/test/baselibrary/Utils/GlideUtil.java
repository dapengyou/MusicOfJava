package com.test.baselibrary.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.test.baselibrary.R;


public class GlideUtil {

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context).load(url).into(imageView);

    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImageWithPlaceholder(Context context, String url, ImageView imageView, @DrawableRes int resId) {
        GlideApp.with(context).load(url).placeholder(resId).into(imageView);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImageWithPlaceholder(Context context, String url, ImageView imageView, Drawable drawable) {
        GlideApp.with(context).load(url).placeholder(drawable).into(imageView);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadHeaderImage(Context context, String url, final ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }


    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadLogoImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);

    }

    /**
     * 是否禁止磁盘缓存加载图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param type      缓存的类型
     *                  <li>磁盘缓存全部 DiskCacheStrategy.ALL</li>
     *                  <li>磁盘禁止缓存DiskCacheStrategy.NONE</li>
     */
    public static void loadImage(Context context, String url, ImageView imageView, DiskCacheStrategy type) {
        GlideApp.with(context).load(url).diskCacheStrategy(type).into(imageView);
    }

    /**
     * 是否禁止内存缓存加载图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param skipMemoryCache 禁止内存缓存 true为禁止
     */
    public static void loadImage(Context context, String url, ImageView imageView, boolean skipMemoryCache) {
        GlideApp.with(context).load(url).skipMemoryCache(skipMemoryCache).into(imageView);
    }

    /**
     * 是否禁止内存/磁盘缓存加载图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param type            缓存的类型
     *                        <li>磁盘缓存全部 DiskCacheStrategy.ALL</li>
     *                        <li>磁盘禁止缓存DiskCacheStrategy.NONE</li>
     * @param skipMemoryCache 禁止内存缓存 true为禁止
     */
    public static void loadImage(Context context, String url, ImageView imageView, DiskCacheStrategy type,
                                 boolean skipMemoryCache) {
        GlideApp.with(context).load(url).skipMemoryCache(skipMemoryCache).diskCacheStrategy(type).into(imageView);
    }

    /**
     * 清除内存中的缓存 必须在UI线程中调用
     *
     * @param context
     */
    public static void clearMemory(Context context) {
        GlideApp.get(context).clearMemory();
    }

    /**
     * 清除磁盘中的缓存 必须在后台线程中调用，建议同时clearMemory()
     *
     * @param context
     */
    public static void clearDiskCache(Context context) {
        GlideApp.get(context).clearDiskCache();
    }

    /**
     * 优先级加载图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param priority  优先级  Priority.LOW/Priority.HIGH
     */
    public static void loadImageWithPriority(Context context, String url, ImageView imageView, Priority priority) {
        GlideApp.with(context).load(url).priority(priority).into(imageView);
    }

    public static void resizeImage(Context context, String url, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)    //加载成功之前占位图
                .error(R.mipmap.ic_launcher)    //加载错误之后的错误图
                .override(width, height)    //指定图片的尺寸
                //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                .fitCenter();

        //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//                .centerCrop();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);

    }


}
