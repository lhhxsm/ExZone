package com.exzone.lib.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.support.multidex.MultiDex;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.exzone.lib.exception.CrashHandler;
import com.exzone.lib.rxjava.ServiceFactory;
import com.exzone.lib.util.Logger;
import com.squareup.leakcanary.LeakCanary;
import java.io.File;
import java.io.InputStream;
import okhttp3.OkHttpClient;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/5.
 */
public class BaseApplication extends Application {
  private static BaseApplication sInstance;
  private static Handler sMainHandler;
  private static int sMainTid;

  public synchronized static BaseApplication getInstance() {
    return sInstance;
  }

  @Override public void onCreate() {
    super.onCreate();
    Logger.initLog(true);// 打开Log输出
    sMainHandler = new Handler();
    sMainTid = Process.myTid();
    sInstance = this;
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }
    LeakCanary.install(this);
    //设置异常处理
    CrashHandler crashHandler = CrashHandler.getInstance();
    crashHandler.init(this);
    initGlide();
  }

  /**
   * 图片加载框架Glide,使用OkHttp处理网络请求
   */
  private void initGlide() {
    OkHttpClient okHttpClient = ServiceFactory.getOkHttpClient();
    Glide.get(this)
        .register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
  }

  public long getMainThreadId() {
    return sMainTid;
  }

  public Handler getMainHandler() {
    return sMainHandler;
  }

  /**
   * 分包
   */
  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Override public File getCacheDir() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      File cacheDir = getExternalCacheDir();
      if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
        return cacheDir;
      }
    }
    return super.getCacheDir();
  }
}
