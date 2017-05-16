package com.android.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者:李鸿浩
 * 描述:单例设计模式的异常捕获
 * 时间:2017/5/11.
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

  private static final String TAG = ExceptionCrashHandler.class.getSimpleName();
  private static ExceptionCrashHandler sInstance;
  private Context mContext;
  private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

  public static ExceptionCrashHandler getInstance() {
    if (sInstance == null) {
      //同步锁:解决多并发的问题
      synchronized (ExceptionCrashHandler.class) {
        if (sInstance == null) {
          sInstance = new ExceptionCrashHandler();
        }
      }
    }
    return sInstance;
  }

  public void init(Context context) {
    this.mContext = context;
    //设置全局的异常类为本类
    Thread.currentThread().setUncaughtExceptionHandler(this);
    mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
  }

  @Override public void uncaughtException(Thread t, Throwable e) {
    //全局异常
    Log.e(TAG, "捕获到异常...");
    // 1.获取信息
    // 1.1崩溃信息
    // 1.2手机信息
    // 1.3版本信息
    // 2.写人文件
    String crashFileName = saveInfoToSD(e);
    Log.e(TAG, "fileName --> " + crashFileName);
    // 3.缓存崩溃日志文件
    cacheCrashFile(crashFileName);
    //让系统默认处理
    mDefaultExceptionHandler.uncaughtException(t, e);
  }

  /**
   * 缓存崩溃日志文件
   *
   * @param fileName 文件名称
   */
  private void cacheCrashFile(String fileName) {
    SharedPreferences preferences = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
    preferences.edit().putString("CRASH_FILE_NAME", fileName).apply();
  }

  /**
   * 获取崩溃文件名称
   */
  public File getCrashFile() {
    String crashFileName = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE)
        .getString("CRASH_FILE_NAME", "");
    return new File(crashFileName);
  }

  /**
   * 保存获取的软件信息、设备信息和出错信息,到SDCard中
   */
  private String saveInfoToSD(Throwable e) {
    String fileName = null;
    StringBuffer sb = new StringBuffer();
    //手机信息 + 应用信息
    for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext).entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      sb.append(key).append(" = ").append(value).append("\n");
    }
    //异常信息
    sb.append(obtainExceptionInfo(e));
    //保存文件
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      File dir = new File(mContext.getFilesDir() + File.separator + "crash" + File.separator);
      //先删除之前的异常信息
      if (dir.exists()) {
        //删除该目录下的所有子文件
        deleteDir(dir);
      }
      //再重新创建文件夹
      if (!dir.exists()) {
        dir.mkdir();
      }
      try {
        fileName = dir.toString() + File.separator + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(sb.toString().getBytes(Charset.defaultCharset()));
        fos.flush();
        fos.close();
      } catch (Exception e1) {
        e1.printStackTrace();
        Log.e(TAG, e1.getMessage());
      }
    }
    return fileName;
  }

  private String getAssignTime(String format) {
    DateFormat dateFormat = new SimpleDateFormat(format);
    long currentTime = System.currentTimeMillis();
    return dateFormat.format(currentTime);
  }

  /**
   * 获取系统未捕获的异常信息
   */
  private String obtainExceptionInfo(Throwable e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    pw.close();
    return sw.toString();
  }

  /**
   * 递归删除目录下的所有文件及子目录下的所有文件
   *
   * @param dir 要删除的文件目录
   */
  private boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
      File[] children = dir.listFiles();
      for (File file : children) {
        if (file.isDirectory()) {
          //递归删除目录中的子目录
          deleteDir(file);
        } else {
          //删除文件
          boolean delete = file.delete();
          if (!delete) {
            return false;
          }
        }
      }
    }
    //目录此时为空,可以删除
    return true;
  }

  /**
   * 获取一下简单的信息,软件版本,手机版本,型号等信息
   */
  private HashMap<String, String> obtainSimpleInfo(Context context) {
    HashMap<String, String> map = new HashMap<>();
    PackageManager packageManager = context.getPackageManager();
    PackageInfo info = null;
    try {
      info = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
      map.put("versionName", info.versionName);
      map.put("versionCode", String.valueOf(info.versionCode));
      map.put("MODEL", Build.MODEL);
      map.put("SDK_INT", String.valueOf(Build.VERSION.SDK_INT));
      map.put("PRODUCT", Build.PRODUCT);
      map.put("MOBILE_INFO", getMobileInfo());
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      Log.e(TAG, e.getMessage());
    }
    return map;
  }

  /**
   * 获取手机信息
   */
  public static String getMobileInfo() {
    StringBuffer sb = new StringBuffer();
    try {
      //利用反射获取Build的所有属性
      Field[] fields = Build.class.getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);
        String name = field.getName();
        String value = field.get(null).toString();
        sb.append(name).append("=").append(value).append("\n");
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }
}
