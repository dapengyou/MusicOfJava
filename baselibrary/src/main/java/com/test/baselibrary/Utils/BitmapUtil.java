package com.test.baselibrary.Utils;

import android.graphics.Bitmap;

/**
 * 图片处理
 * Created by Robin on 2017/4/24.
 */

public class BitmapUtil {

    /**
     * 按正方形裁剪图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap cropImgToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth(); // 得到图片的宽，高
        int height = bitmap.getHeight();

        int length = width > height ? height : width;// 裁切后所取的正方形区域边长

        int retX = width > height ? (width - height) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = width > height ? 0 : (height - width) / 2;

        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, length, length, null, false);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;
    }

    /**
     * 将图片放大或缩小到指定尺寸
     */
    public static Bitmap resizeImage(Bitmap source, int dstWidth, int dstHeight) {
        if (source == null) {
            return null;
        }

        if (source.getWidth() == dstWidth && source.getHeight() == dstHeight) {
            return source;
        }

        return Bitmap.createScaledBitmap(source, dstWidth, dstHeight, true);
    }

}
