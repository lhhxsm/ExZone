package com.exzone.lib.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 描述:Toast工具类
 * 作者:李鸿浩
 * 时间:2016/11/20.
 */

public class ToastUtils {
  private static Toast sToast;

  /**
   * 自定义显示Toast时间
   */
  public static void show(Context context, CharSequence message, int duration) {
    if (sToast == null) {
      sToast = Toast.makeText(context, message, duration);
    } else {
      sToast.setText(message);
      sToast.setDuration(duration);
    }
    sToast.show();
  }

  /**
   * 自定义显示Toast时间
   */
  public static void show(Context context, int resId, int duration) {
    show(context, context.getResources().getText(resId), duration);
  }

  /**
   * 短时间显示Toast
   */
  public static void showShort(Context context, CharSequence message) {
    show(context, message, Toast.LENGTH_SHORT);
  }

  /**
   * 短时间显示Toast
   */
  public static void showShort(Context context, int resId) {
    showShort(context, context.getResources().getText(resId));
  }

  /**
   * 长时间显示Toast
   */
  public static void showLong(Context context, CharSequence message) {
    show(context, message, Toast.LENGTH_LONG);
  }

  /**
   * 长时间显示Toast
   */
  public static void showLong(Context context, int resId) {
    showLong(context, context.getResources().getText(resId));
  }
}
