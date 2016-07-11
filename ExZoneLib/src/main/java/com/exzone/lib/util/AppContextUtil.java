package com.exzone.lib.util;

import android.content.Context;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/5.
 */
public class AppContextUtil {
    private static Context sContext;

    private AppContextUtil() {
        throw new AssertionError();
    }

    public static void init(Context context) {
        sContext = context;
    }

    public static Context getInstance() {
        if (sContext == null) {
            throw new NullPointerException("the context is null,please init AppContextUtil in Application first.");
        }
        return sContext;
    }
}
