package com.android.demo;

import android.app.Application;
import com.android.lib.ExceptionCrashHandler;
import com.android.lib.FixDexManager;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/5/11.
 */
public class GlobalApplication extends Application {

  //public static PatchManager sPatchManager;

  @Override public void onCreate() {
    super.onCreate();

    //设置全局异常捕获
    ExceptionCrashHandler.getInstance().init(this);
    ////初始化阿里的热修复
    //sPatchManager = new PatchManager(this);
    ////初始化版本,获取当前应用的版本
    //sPatchManager.init("1.0");
    ////加载之前的apatch包
    //sPatchManager.loadPatch();

    try {
      //加载所有修复的Dex包
      FixDexManager manager = new FixDexManager(this);
      manager.loadFixDex();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
