package com.exzone.lib.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * 作者:李鸿浩
 * 描述:日志工具类
 * 时间: 2016/9/30.
 */
public class Logger {

  private static String sTag;
  private static boolean sDebug;

  private Logger() {
    throw new AssertionError();
  }

  /**
   * 初始化Tag(Debug取默认值false)
   *
   * @param tag Tag
   */
  public static void initLog(String tag) {
    initLog(tag, false);
  }

  /**
   * 初始化Debug(Tag取默认值)
   *
   * @param debug 是否开启调试
   */
  public static void initLog(boolean debug) {
    initLog(sTag, debug);
  }

  /**
   * 初始化Tag和Debug
   *
   * @param tag Tag
   * @param debug 是否开启调试
   */
  public static void initLog(String tag, boolean debug) {
    sTag = tag;
    sDebug = debug;
  }

  public static void v(String str, Object... args) {
    if (isDebug()) {
      Log.v(getTag(), buildLogString(str, args));
    }
  }

  public static void d(String str, Object... args) {
    if (isDebug()) {
      Log.d(getTag(), buildLogString(str, args));
    }
  }

  public static void i(String str, Object... args) {
    if (isDebug()) {
      Log.i(getTag(), buildLogString(str, args));
    }
  }

  public static void w(String str, Object... args) {
    if (isDebug()) {
      Log.w(getTag(), buildLogString(str, args));
    }
  }

  public static void e(String str, Object... args) {
    if (isDebug()) {
      Log.e(getTag(), buildLogString(str, args));
    }
  }

  private static String getTag() {
    if (!TextUtils.isEmpty(sTag)) {
      return sTag;
    }
    // 如果sTAG为空则从StackTrace中取TAG
    StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
    return caller.getFileName();
  }

  private static String buildLogString(String str, Object... args) {
    if (args.length > 0) {
      str = String.format(str, args);
    }
    StackTraceElement element = new Throwable().fillInStackTrace().getStackTrace()[2];
    return "("
        + element.getFileName()
        + ":"
        + element.getLineNumber()
        + ")."
        + element.getMethodName()
        + "():"
        + str;
    //        StringBuilder sb = new StringBuilder();
    //        sb.append("(")
    //                .append(element.getFileName())
    //                .append(":")
    //                .append(element.getLineNumber())
    //                .append(").")
    //                .append(element.getMethodName())
    //                .append("():")
    //                .append(str);
    //        return sb.toString();
  }

  private static boolean isDebug() {
    return sDebug || Log.isLoggable(getTag(), Log.DEBUG);
  }
}
