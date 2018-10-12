package com.test.baselibrary.Utils;
/**
 * @date 创建时间: 2018/10/10
 * @author  lady_zhou
 * @Description
 */
public class StringUtills {

    /**
     * @date 创建时间: 2018/10/10
     * @author  lady_zhou
     * @Description 判断是否为空
     * @param str 字符串
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }


    /**
     * 判断是否不为null或不是空字符串
     *
     * @param str
     * @return
     */

    public static boolean isNotEmpty(String str) {
        if (str == null || str.trim().equals(""))
            return false;
        return true;
    }

}
