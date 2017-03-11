package com.exzone.lib.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.support.multidex.MultiDex;

import com.exzone.lib.exception.CrashHandler;
import com.exzone.lib.util.Logger;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        //设置异常处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    /**
     * 分包
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
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
