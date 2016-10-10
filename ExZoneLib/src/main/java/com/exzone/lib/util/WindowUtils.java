package com.exzone.lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Surface;

/**
 * 作者:李鸿浩
 * 描述:窗口工具类
 * 时间: 2016/10/10.
 */
public class WindowUtils {
    private WindowUtils() {
        throw new AssertionError();
    }

    /**
     * 获取当前窗口的旋转角度
     */
    public static int getDisplayRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    /**
     * 当前是否是横屏
     */
    public static final boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 当前是否是竖屏
     */
    public static final boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
