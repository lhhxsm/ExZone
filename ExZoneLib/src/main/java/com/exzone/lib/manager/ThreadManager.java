package com.exzone.lib.manager;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import com.exzone.lib.util.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者:李鸿浩
 * 描述:线程管理
 * 时间: 2017/1/23.
 */

public class ThreadManager {
  private static ThreadManager threadManager;
  private Map<String, ThreadBean> threadMap;

  private ThreadManager() {
    threadMap = new HashMap<String, ThreadBean>();
  }

  public static synchronized ThreadManager getInstance() {
    if (threadManager == null) {
      threadManager = new ThreadManager();
    }
    return threadManager;
  }

  /**
   * 运行线程
   */
  public Handler runThread(String name, Runnable runnable) {
    if (TextUtils.isEmpty(name) || runnable == null) {
      return null;
    }
    Logger.d("\n runThread  name = " + name);
    Handler handler = getHandler(name);
    if (handler != null) {
      Logger.d("handler != null >>  destroyThread(name);");
      destroyThread(name);
    }
    HandlerThread thread = new HandlerThread(name);
    thread.start();//创建一个HandlerThread并启动它
    handler = new Handler(thread.getLooper());//使用HandlerThread的looper对象创建Handler
    handler.post(runnable);//将线程post到Handler中
    threadMap.put(name, new ThreadBean(name, thread, runnable, handler));
    Logger.d("runThread  added name = " + name + "; threadMap.size() = " + threadMap.size() + "\n");
    return handler;
  }

  /**
   * 获取线程Handler
   */
  private Handler getHandler(String name) {
    ThreadBean tb = getThread(name);
    return tb == null ? null : tb.getHandler();
  }

  /**
   * 获取线程
   */
  private ThreadBean getThread(String name) {
    return name == null ? null : threadMap.get(name);
  }

  /**
   * 销毁线程
   */
  public void destroyThread(List<String> nameList) {
    if (nameList != null) {
      for (String name : nameList) {
        destroyThread(name);
      }
    }
  }

  /**
   * 销毁线程
   */
  public void destroyThread(String name) {
    destroyThread(getThread(name));
  }

  /**
   * 销毁线程
   */
  private void destroyThread(ThreadBean tb) {
    if (tb == null) {
      Logger.e("destroyThread  tb == null >> return;");
      return;
    }

    destroyThread(tb.getHandler(), tb.getRunnable());
    if (tb.getName() != null) {
      threadMap.remove(tb.getName());
    }
  }

  /**
   * 销毁线程
   */
  public void destroyThread(Handler handler, Runnable runnable) {
    if (handler == null || runnable == null) {
      Logger.e("destroyThread  handler == null || runnable == null >> return;");
      return;
    }

    try {
      handler.removeCallbacks(runnable);
    } catch (Exception e) {
      e.printStackTrace();
      Logger.e(
          "onDestroy try { handler.removeCallbacks(runnable);...  >> catch  : " + e.getMessage());
    }
  }

  /**
   * 结束ThreadManager所有进程
   */
  public void finish() {
    threadManager = null;
    if (threadMap == null) {
      return;
    }
    List<String> nameList = new ArrayList<String>(threadMap.keySet());//直接用Set在系统杀掉应用时崩溃
    for (String name : nameList) {
      destroyThread(name);
    }
    threadMap = null;
    Logger.d("\n finish  finished \n");
  }

  /**
   * 线程类
   */
  private static class ThreadBean {

    private String name;
    private HandlerThread thread;
    private Runnable runnable;
    private Handler handler;

    public ThreadBean(String name, HandlerThread thread, Runnable runnable, Handler handler) {
      this.name = name;
      this.thread = thread;
      this.runnable = runnable;
      this.handler = handler;
    }

    public String getName() {
      return name;
    }

    public Runnable getRunnable() {
      return runnable;
    }

    public Handler getHandler() {
      return handler;
    }
  }
}
