package com.exzone.lib.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

/**
 * 作者:lhh
 * 描述:网络 工具类
 * 时间：2016/10/10.
 */
public class NetWorkUtils {

  /**
   * 接受网络状态的广播Action
   */
  public static final String NET_BROADCAST_ACTION = "com.network.state.action";

  public static final String NET_STATE_NAME = "network_state";
  //接受服务上发过来的广播
  private static BroadcastReceiver mReceiver = new BroadcastReceiver() {

    @Override public void onReceive(Context context, Intent intent) {
      if (intent != null) {
        int state = (int) intent.getExtras().get(NET_STATE_NAME);
        switch (state) {
          case -1:
            Logger.e("无网络  state =" + state);
            break;
          case 1:
            Logger.e("WIFI网络  state=" + state);
            break;
          case 0:
            Logger.e("移动网络  state =" + state);
            break;
          default:
            break;
        }
      }
    }
  };

  /**
   * 判断网络是否连接
   */
  public static boolean isConnected(Context context) {
    ConnectivityManager manager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (null != manager) {
      NetworkInfo info = manager.getActiveNetworkInfo();
      if (null != info && info.isConnected()) {
        if (info.getState() == NetworkInfo.State.CONNECTED) {
          Logger.e("当前网络可用");
          return true;
        }
      }
    }
    Logger.e("当前网络不可用");
    return false;
  }

  /**
   * 判断是否是wifi连接
   */
  public static boolean isWifi(Context context) {
    ConnectivityManager manager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (manager == null) {
      Logger.e("当前网络----->不可用");
      return false;
    }
    boolean isWifi = manager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    if (isWifi) {
      Logger.e("当前网络----->WIFI环境");
    } else {
      Logger.e("当前网络----->非WIFI环境");
    }
    return isWifi;
  }

  /**
   * 判断是否是Mobile连接
   */
  public static boolean isMobile(Context context) {
    ConnectivityManager manager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (manager == null) {
      Logger.e("当前网络----->不可用");
      return false;
    }
    boolean isMobile = manager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE;
    if (isMobile) {
      Logger.e("当前网络----->Mobile环境");
    } else {
      Logger.e("当前网络----->非Mobile环境");
    }
    return isMobile;
  }

  /**
   * TYPE_NONE   = -1
   * TYPE_MOBILE = 0
   * TYPE_WIFI   = 1
   */
  public static int getConnectedType(Context context) {
    ConnectivityManager manager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = manager.getActiveNetworkInfo();
    if (info != null && info.isAvailable()) {
      return info.getType();
    }
    return -1;
  }

  /**
   * 打开网络设置界面
   */
  public static void openSetting(Activity activity) {
    Intent intent = new Intent("/");
    ComponentName cm =
        new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
    intent.setComponent(cm);
    intent.setAction("android.intent.action.VIEW");
    activity.startActivityForResult(intent, 0);
  }

  /**
   * 开启服务,实时监听网络变化
   * 需要自己在清单文件中配置服务
   * 然后把对应的Action传入
   * 服务类:com.exzone.lib.service.NetWorkService
   */
  public static void startNetService(Context context, String action) {
    //注册广播
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(NET_BROADCAST_ACTION);
    context.registerReceiver(mReceiver, intentFilter);
    //开启服务
    Intent intent = new Intent();
    Logger.e("开启网络监听服务");
    intent.setAction(action);
    intent.setPackage(context.getPackageName());//从Lollipop(API 5.0)开始,service服务必须采用显示方式启动。
    context.bindService(intent, new ServiceConnection() {

      @Override public void onServiceDisconnected(ComponentName name) {

      }

      @Override public void onServiceConnected(ComponentName name, IBinder service) {

      }
    }, Context.BIND_AUTO_CREATE);
  }
}
