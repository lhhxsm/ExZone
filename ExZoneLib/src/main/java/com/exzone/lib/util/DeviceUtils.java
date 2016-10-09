package com.exzone.lib.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 作者:李鸿浩
 * 描述:手机信息工具类
 * 时间：2016/10/9.
 */
public class DeviceUtils {
    /**
     * 获取应用程序的IMEI号
     */
    public static String getIMEI(Context context) {
        if (context == null) {
            Logger.e("context不能为空");
            return null;
        }
        TelephonyManager telecomManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telecomManager.getDeviceId();
        Logger.e("IMEI标识：" + imei);
        return imei;
    }

    /**
     * 获取设备的系统版本号
     */
    public static int getDeviceSDK() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        Logger.e("设备版本：" + sdk);
        return sdk;
    }

    /**
     * 获取设备的型号
     */
    public static String getDeviceName() {
        String model = android.os.Build.MODEL;
        Logger.e("设备型号：" + model);
        return model;
    }
}
