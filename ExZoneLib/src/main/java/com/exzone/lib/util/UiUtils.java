package com.exzone.lib.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.exzone.lib.base.BaseApplication;

/**
 * 作者:李鸿浩
 * 描述:UI工具类
 * 时间: 2016/9/30.
 */
public class UiUtils {
    private UiUtils() {
        throw new AssertionError();
    }

    /**
     * 移除自身布局
     *
     * @param v 被移除的View
     */
    public static void removeParent(View v) {
        // 先找到爹 在通过爹去移除孩子
        ViewParent parent = v.getParent();
        // 所有的控件 都有爹 爹一般情况下 就是ViewGroup
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(v);
        }
    }

    /**
     * 把Runnable 方法提交到主线程运行
     */
    public static void runOnUiThread(Runnable runnable, long delayMillis) {
        // 在主线程运行
        if (android.os.Process.myTid() == BaseApplication.sMainTid) {
            if (delayMillis > 0)
                BaseApplication.sHandler.postDelayed(runnable, delayMillis);
            else
                runnable.run();
        } else { // 获取handler
            if (BaseApplication.sHandler != null) {
                if (delayMillis == 0)
                    BaseApplication.sHandler.post(runnable);
                else
                    BaseApplication.sHandler.postDelayed(runnable, delayMillis);
            }
        }
    }
}
