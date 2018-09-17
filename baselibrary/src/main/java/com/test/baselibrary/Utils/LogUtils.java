package com.test.baselibrary.Utils;

import android.util.Log;


/**
 * Log日志，方便统一管理
 */
public class LogUtils {

    private static boolean isPrintLog = true;

    /**
     * 设置是否打印日志
     *
     * @param isPrint 是否打印日志
     */
    public static void setPrintLog(boolean isPrint) {
        isPrintLog = isPrint;
    }

    // Log.d()
    public static void d(String msg) {
        d(LogUtils.class, msg);
    }

    public static void d(Object msg) {
        d(LogUtils.class, msg);
    }

    public static void d(Class<?> tag, String msg) {
        d(tag.getSimpleName(), msg);
    }

    public static void d(Class<?> tag, Object msg) {
        if (msg == null) {
            d(tag.getSimpleName(), null);
        } else {
            d(tag.getSimpleName(), msg.toString());
        }
    }

    public static void d(String tag, String msg) {
        if (msg == null) {
            msg = "null";
        }
        if (isPrintLog) {
            Log.d(tag, msg);
        }
    }

    // Log.e()
    public static void e(String msg) {
        e(LogUtils.class, msg);
    }

    public static void e(Object msg) {
        e(LogUtils.class, msg);
    }

    public static void e(Class<?> tag, String msg) {
        e(tag.getSimpleName(), msg);
    }

    public static void e(Class<?> tag, Object msg) {
        if (msg == null) {
            e(tag.getSimpleName(), null);
        } else {
            e(tag.getSimpleName(), msg.toString());
        }
    }

    public static void e(String tag, String msg) {
        if (msg == null) {
            msg = "null";
        }
        if (isPrintLog) {
            Log.e(tag, msg);
        }
    }

    // Log.i()
    public static void i(String msg) {
        i(LogUtils.class, msg);
    }

    public static void i(Object msg) {
        i(LogUtils.class, msg);
    }

    public static void i(Class<?> tag, String msg) {
        i(tag.getSimpleName(), msg);
    }

    public static void i(Class<?> tag, Object msg) {
        if (msg == null) {
            i(tag.getSimpleName(), null);
        } else {
            i(tag.getSimpleName(), msg.toString());
        }
    }

    public static void i(String tag, String msg) {
        if (msg == null) {
            msg = "null";
        }
        if (isPrintLog) {
            Log.i(tag, msg);
        }
    }

    // Log.v()
    public static void v(String msg) {
        v(LogUtils.class, msg);
    }

    public static void v(Object msg) {
        v(LogUtils.class, msg);
    }

    public static void v(Class<?> tag, String msg) {
        v(tag.getSimpleName(), msg);
    }

    public static void v(Class<?> tag, Object msg) {
        if (msg == null) {
            v(tag.getSimpleName(), null);
        } else {
            v(tag.getSimpleName(), msg.toString());
        }
    }

    public static void v(String tag, String msg) {
        if (msg == null) {
            msg = "null";
        }
        if (isPrintLog) {
            Log.v(tag, msg);
        }
    }

    // Log.w()
    public static void w(String msg) {
        w(LogUtils.class, msg);
    }

    public static void w(Object msg) {
        w(LogUtils.class, msg);
    }

    public static void w(Class<?> tag, String msg) {
        w(tag.getSimpleName(), msg);
    }

    public static void w(Class<?> tag, Object msg) {
        if (msg == null) {
            w(tag.getSimpleName(), null);
        } else {
            w(tag.getSimpleName(), msg.toString());
        }
    }

    public static void w(String tag, String msg) {
        if (msg == null) {
            msg = "null";
        }
        if (isPrintLog) {
            Log.w(tag, msg);
        }
    }

}