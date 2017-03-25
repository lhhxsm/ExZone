package com.exzone.lib.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.text.TextUtils;

import java.util.List;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间: 2017/1/20.
 */

public class AppUtils {
    private AppUtils() {
        throw new AssertionError();
    }

    /**
     * 获取应用程序版本（versionCode）
     */
    public static int getVersionCode(Context context) {
        if (context == null)
            return -1;
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("获取应用程序版本失败,原因：" + e.getMessage());
            return -1;
        }
        return info.versionCode;
    }

    /**
     * 获取应用程序版本（versionName）
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        if (context == null)
            return null;
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("获取应用程序版本失败,原因：" + e.getMessage());
            return "";
        }
        return info.versionName;
    }

    /**
     * 是否是应用程序
     */
    public static boolean isSystemApplication(Context context) {
        return context != null && isSystemApplication(context, context.getPackageName());
    }

    /**
     * 是否是应用程序
     */
    public static boolean isSystemApplication(Context context, String packageName) {
        return context != null && isSystemApplication(context.getPackageManager(), packageName);
    }

    /**
     * 是否是应用程序
     */
    public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
        if (packageManager == null || packageName == null || packageName.length() == 0) {
            return false;
        }

        try {
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * whether this process is named with processName
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null || TextUtils.isEmpty(processName)) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (ListUtils.isEmpty(processInfoList)) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo != null && processInfo.pid == pid && (processName.equals(processInfo.processName))) {
                return true;
            }
        }
        return false;
    }

    /**
     * whether application is in background
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /***
     * 获取MAC地址
     */
    public static String getMacAddress(Context context) {
        if (context == null)
            return null;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    /**
     * 获取运行时间
     *
     * @return 运行时间(单位/s)
     */
    public static long getRunTimes() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        if (ut == 0) {
            ut = 1;
        }
        return ut;
    }

    /**
     * 安装文件
     *
     * @param data
     */
    public static void promptInstall(Context context, Uri data) {
        Intent promptInstall = new Intent(Intent.ACTION_VIEW).setDataAndType(data, "application/vnd.android.package-archive");
        // FLAG_ACTIVITY_NEW_TASK 可以保证安装成功时可以正常打开 app
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(promptInstall);
    }

}
