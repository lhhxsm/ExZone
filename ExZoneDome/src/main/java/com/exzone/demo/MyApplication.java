package com.exzone.demo;

import android.content.Context;
import android.support.multidex.MultiDex;
import com.exzone.lib.base.BaseApplication;

/**
 * 作者:lhh
 * 描述:
 * 时间：2016/10/7.
 */
public class MyApplication extends BaseApplication {

  @Override public void onCreate() {
    super.onCreate();
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }
}
