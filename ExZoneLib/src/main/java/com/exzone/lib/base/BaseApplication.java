package com.exzone.lib.base;

import android.app.Application;
import android.content.Context;

import com.exzone.lib.util.AppContextUtil;
import com.exzone.lib.widget.logger.Logger;

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
        initLogger();
    }

    /**
     * 设置全局日志打印
     */
    private void initLogger() {
        Logger.init(logTag())//如果仅仅调用 init 不传递参数，默认标签是 Logger
                .setMethodCount(3)//显示调用方法链的数量，默认是2
                .setMethodOffset(2)
                .setShowThreadInfo(isShow())//隐藏线程信息，默认是显示
                .setDebug(isDebug());//
    }

    /**
     * 重写方法进行设置
     */
    public boolean isDebug() {
        return true;
    }

    public boolean isShow() {
        return true;
    }

    public String logTag() {
        return null;
    }

    // 获取ApplicationContext
    public static Context getContext() {
        return sContext;
    }
}
