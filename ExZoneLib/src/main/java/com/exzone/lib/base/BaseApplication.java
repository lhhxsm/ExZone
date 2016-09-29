package com.exzone.lib.base;

import android.app.Application;
import android.content.Context;

import com.exzone.lib.util.AppContextUtil;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/5.
 */
public class BaseApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        AppContextUtil.init(this);

    }

    // 获取ApplicationContext
    public static Context getContext() {
        return sContext;
    }
}
