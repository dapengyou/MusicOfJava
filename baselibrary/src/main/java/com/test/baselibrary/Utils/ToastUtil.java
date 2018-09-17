package com.test.baselibrary.Utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Context mContext;
    private static final String TAG = "ToastUtil";
    private static Toast toast;

    private ToastUtil() {

    }

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 短吐司
     */
    public static void showToast(String msg) {
        toast(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 长吐司
     */
    public static void showLongToast(String msg) {
        toast(msg, Toast.LENGTH_LONG);
    }

    private static void toast(String msg, int lengthLong) {
        if (mContext == null) {
            LogUtils.i(TAG, "Toast未初始化");
            return;
        }
        if (toast != null) {
            toast.cancel();//关闭吐司显示
        }
        toast = Toast.makeText(mContext, msg, lengthLong);
        toast.show();//重新显示吐司
    }
}
