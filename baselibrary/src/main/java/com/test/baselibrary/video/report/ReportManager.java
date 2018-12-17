package com.test.baselibrary.video.report;

import com.test.baselibrary.Utils.Utils;
import com.test.baselibrary.video.bean.Monitor;

import java.util.ArrayList;

/**
 * @createTime: 2018/10/25
 * @author: lady_zhou
 * @Description: 负责所有监测请求的发送
 */
public class ReportManager {
    /**
     * @param monitors :
     * @param isFull   :
     * @param playTime :
     * @return : void
     * @date 创建时间: 2018/10/25
     * @author lady_zhou
     * @Description 发送sue report
     */
    public static void sueReport(ArrayList<Monitor> monitors, boolean isFull, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
//                RequestParams params = new RequestParams();
//                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
//                    if (isFull) {
//                        params.put("fu", "1");
//                    }
//                    params.put("ve", String.valueOf(playTime));
//                }

                //调用网络API通知服务器内容播完
//                CommonOkHttpClient.get(
//                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * @param monitors :
     * @param playTime :
     * @return : void
     * @date 创建时间: 2018/10/25
     * @author lady_zhou
     * @Description 发送su report
     */
    public static void suReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
//                RequestParams params = new RequestParams();
//                if (monitor.time == playTime) {
//                    if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
//                        params.put("ve", String.valueOf(playTime));
//                    }
//                    CommonOkHttpClient.get(
//                            CommonRequest.createMonitorRequest(monitor.url, params), handle);
//                }
            }
        }
    }

    /**
     * send the clicl full btn monitor
     *
     * @param monitors urls
     * @param playTime player time
     */
    public static void fullScreenReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
//                RequestParams params = new RequestParams();
//                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
//                    params.put("ve", String.valueOf(playTime));
//                }
//                CommonOkHttpClient.get(
//                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * send the click back full btn monitor
     *
     * @param monitors urls
     * @param playTime player time
     */
    public static void exitfullScreenReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
//                RequestParams params = new RequestParams();
//                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
//                    params.put("ve", String.valueOf(playTime));
//                }
//                CommonOkHttpClient.get(
//                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * send the video pause monitor
     *
     * @param monitors urls
     * @param playTime player time
     */
    public static void pauseVideoReport(ArrayList<Monitor> monitors, long playTime) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
//                RequestParams params = new RequestParams();
//                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
//                    params.put("ve", String.valueOf(playTime));
//                }
//                CommonOkHttpClient.get(
//                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }

    /**
     * send the sus monitor
     */
    public static void susReport(ArrayList<Monitor> monitors, boolean isAuto) {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {
//                RequestParams params = new RequestParams();
//                if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
//                    params.put("ve", "0");
//                    if (isAuto) {
//                        params.put("auto", "1");
//                    }
//                }
//                CommonOkHttpClient.get(
//                        CommonRequest.createMonitorRequest(monitor.url, params), handle);
            }
        }
    }
}
