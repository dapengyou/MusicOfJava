package com.test.baselibrary.Utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 * <p>
 * Created by jiang on 2015/10/8.
 */
public class DateUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期时间段描述
     *
     * @param timeMillis 过去的某个时间点毫秒值
     * @return 时间段描述字符串，例如：刚刚/10分钟前/1小时前...
     */
    public static String when(long timeMillis) {
        long nowMillis = System.currentTimeMillis();
        long sub = nowMillis - timeMillis;
        long min = 60 * 1000;
        long res = sub / min;
        if (res == 0) { // 一分钟内
            return "刚刚";
        }
        if (res == 30) {
            return "半小时前";
        }
        if (res < 60) {
            return res + "分钟前";
        }
        long minOfDay = 60 * 24;
        if (res < minOfDay) {
            return res / 60 + "小时前";
        }
        if (res < 2 * minOfDay) {
            return "昨天";
        }
        if (res < 3 * minOfDay) {
            return "前天";
        }
        if (isCurrentYear(timeMillis)) {
            return format("MM-dd HH:mm", timeMillis);
        }
        return format("yyyy-MM-dd HH:mm", timeMillis);
    }

    /**
     * 格式化日期
     *
     * @param timeMillis 待格式化日期毫秒值
     * @return 被格式化后的字符串, 格式参见: DateUtils.DEFAULT_DATE_FORMAT
     */
    public static String format(long timeMillis) {
        return format(DEFAULT_DATE_FORMAT, timeMillis);
    }

    /**
     * 格式化日期
     *
     * @param date 待格式化日期
     * @return 被格式化后的字符串, 格式参见：DateUtils.DEFAULT_DATE_FORMAT
     */
    public static String format(Date date) {
        return format(DATE_FORMAT, date);
    }


    public static String format(String dateStr) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format(DATE_FORMAT, date);
    }

    /**
     * 格式化日期
     *
     * @param format     日期格式模板
     * @param timeMillis 待格式化日期毫秒值
     * @return 被格式化后的字符串
     */
    public static String format(String format, long timeMillis) {
        Date date = new Date(timeMillis);
        return format(format, date);
    }

    /**
     * 格式化日期
     *
     * @param format 日期格式模板
     * @param date   待格式化日期
     * @return 被格式化后的字符串
     */
    public static String format(String format, Date date) {
        if (TextUtils.isEmpty(format)) {
            format = DEFAULT_DATE_FORMAT;
        }
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return dateFormat.format(date);
    }

    /**
     * 判断日期是否在该年内
     *
     * @param timeMillis 时间毫秒值
     * @return 如果指定时间是该年的将返回true，否则返回false
     */
    public static boolean isCurrentYear(long timeMillis) {
        Date destDate = new Date(timeMillis);
        return isCurrentYear(destDate);
    }

    /**
     * 判断日期是否在该年内
     *
     * @param date 日期
     * @return 如果指定时间是该年的将返回true，否则返回false
     */
    public static boolean isCurrentYear(Date date) {
        Date now = new Date();
        String nowYear = format("yyyy", now);
        String destYear = format("yyyy", date);
        return nowYear.equalsIgnoreCase(destYear);
    }

    /**
     * 获取日期是星期几
     *
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取今天是星期几
     *
     * @return
     */
    public static String getWeekOfDate() {
        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        return dateFm.format(date);
    }


}
