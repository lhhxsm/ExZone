package com.android.lib.utils;

import android.text.TextUtils;
import android.util.Log;
import com.android.lib.BuildConfig;
import com.android.lib.Constants;

/**
 * 日志工具类
 */
public class Logger {
  private Logger() {
    throw new AssertionError();
  }

  public static void e(String msg) {
    e(Constants.TAG, msg);
  }

  public static void e(String tag, String msg) {
    if (!BuildConfig.DEBUG) return;
    if (TextUtils.isEmpty(msg)) return;

    StackTraceElement element = new Throwable().fillInStackTrace().getStackTrace()[2];
    String s = "("
        + element.getFileName()
        + ":"
        + element.getLineNumber()
        + ")."
        + element.getMethodName()
        + "():"
        + msg;
    Log.e(tag, msg);
  }
}
