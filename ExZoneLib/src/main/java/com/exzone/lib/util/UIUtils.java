package com.exzone.lib.util;

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

    /**
     * 把Runnable 方法提交到主线程运行
     */
    public static void runOnUiThread(Runnable runnable, long delayMillis) {
        // 在主线程运行
        if (android.os.Process.myTid() == BaseApplication.getInstance().getMainThreadId()) {
            if (delayMillis > 0)
                BaseApplication.getInstance().getMainHandler().postDelayed(runnable, delayMillis);
            else
                runnable.run();
        } else { // 获取handler
            if (BaseApplication.getInstance().getMainHandler() != null) {
                if (delayMillis == 0)
                    BaseApplication.getInstance().getMainHandler().post(runnable);
                else
                    BaseApplication.getInstance().getMainHandler().postDelayed(runnable, delayMillis);
            }
        }
    }
}