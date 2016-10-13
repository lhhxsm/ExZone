package com.exzone.lib.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Process;

import com.exzone.lib.fresco.FrescoManager;
import com.exzone.lib.util.Logger;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/5.
 */
public class BaseApplication extends Application {

    public static Context sContext;
    public static Handler sHandler;
    public static int sMainTid;

    @Override
    public void onCreate() {
        Logger.initLog(true);// 打开Log输出
        sContext = getApplicationContext();
        sHandler = new Handler();
        sMainTid = Process.myTid();
        initWidthAndHeight();
        super.onCreate();
//        FrescoManager.init(this);
    }

    /**
     * 获取屏幕长度高度
     */
    private void initWidthAndHeight() {
        // 防止被系统字体改变默认得字体大小
        Resources resource = getResources();
        Configuration c = resource.getConfiguration();
        c.fontScale = 1.0f;
        resource.updateConfiguration(c, resource.getDisplayMetrics());
    }
}
