package com.android.lib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/5/4.
 */
public class NetUtil {

  /**
   * 是否有网络
   */
  public static boolean isAvailable(Context context) {
    try {
      ConnectivityManager manager =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info = manager.getActiveNetworkInfo();
      return info != null && info.isConnected();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
