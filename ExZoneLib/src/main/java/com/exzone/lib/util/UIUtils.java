package com.exzone.lib.util;

import android.os.Handler;

import com.exzone.lib.base.BaseApplication;

/**
 * 作者:李鸿浩
 * 描述:UI工具类
 * 时间: 2016/9/30.
 */
public class UIUtils {
    private UIUtils() {
        throw new AssertionError();
    }

    public static Handler getHandler() {
        return BaseApplication.getInstance().getMainHandler();
    }

    public static long getMainThreadId() {
        return BaseApplication.getInstance().getMainThreadId();
    }

    /**
     * 把Runnable 方法提交到主线程运行
     */
    public static void runOnUiThread(Runnable runnable, long delayMillis) {
        Handler handler = BaseApplication.getInstance().getMainHandler();
        // 在主线程运行
        if (android.os.Process.myTid() == BaseApplication.getInstance().getMainThreadId()) {
            if (delayMillis > 0) {
                handler.postDelayed(runnable, delayMillis);
            } else {
                runnable.run();
            }
        } else { // 获取handler
            if (handler != null) {
                if (delayMillis <= 0) {
                    handler.post(runnable);
                } else {
                    handler.postDelayed(runnable, delayMillis);
                }
            }
        }
    }

    /**
     * 让task在主线程中执行
     */
    public static void runOnUiThread(Runnable runnable) {
        if (android.os.Process.myTid() == BaseApplication.getInstance().getMainThreadId()) {
            runnable.run();
        } else {
            BaseApplication.getInstance().getMainHandler().post(runnable);
        }
    }

    /**
     * 执行延时任务
     */
    public static void postDelayed(Runnable runnable, long delayed) {
        BaseApplication.getInstance().getMainHandler().postDelayed(runnable, delayed);
    }

    /**
     * 移除任务
     */
    public static void removeCallbacks(Runnable runnable) {
        BaseApplication.getInstance().getMainHandler().removeCallbacks(runnable);
    }
}
