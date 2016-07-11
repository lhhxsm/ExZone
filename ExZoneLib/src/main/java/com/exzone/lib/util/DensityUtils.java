package com.exzone.lib.util;

import android.content.Context;

/**
 * 作者:李鸿浩
 * 描述:单位转换工具类
 * 时间:2016/7/9.
 */
public class DensityUtils {
    private DensityUtils() {
        throw new AssertionError();
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param px      像素
     * @return sp值
     */
    public static int px2sp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scale + 0.5f);
    }
}
