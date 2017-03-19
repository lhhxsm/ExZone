package com.exzone.lib.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.exzone.lib.util.Logger;

import java.util.Stack;

/**
 * 功能:Activity管理类
 * 作者:李鸿浩
 * 时间:2016/11/17.
 */
public class ActManager {
    private static Stack<Activity> sStack;

    private volatile static ActManager sInstance;

    private ActManager() {
    }

    /**
     * 单例模式
     */
    public static ActManager getInstance() {
        if (sInstance == null) {
            synchronized (ActManager.class) {
                if (sInstance == null) {
                    sInstance = new ActManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 添加Activity到堆栈中
     */
    public void addActivity(Activity activity) {
        if (sStack == null) {
            sStack = new Stack<>();
        }
        sStack.add(activity);
    }

    /**
     * 获取当前的Activity,即堆栈中的最后一个
     */
    public Activity currentActivity() {
        Activity activity = sStack.lastElement();
        return activity;
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            sStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param clazz
     */
    public void finishActivity(Class<?> clazz) {
        for (Activity activity : sStack) {
            if (activity.getClass().equals(clazz)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束当前的Activity
     */
    public void finishActivity() {
        Activity activity = currentActivity();
        finishActivity(activity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = sStack.size(); i < size; i++) {
            if (null != sStack.get(i)) {
                sStack.get(i).finish();
            }
        }
        sStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void exit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0); // 注意，如果您有后台程序运行，请不要支持此句子
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有Activity，但保留最后一个
     */
    public synchronized void keepLastOne() {
        for (int i = 0, size = sStack.size() - 1; i < size; i++) {
            sStack.get(0).finish();
            sStack.remove(0);
        }
    }

    /**
     * 堆栈中activity的个数
     *
     * @return
     */
    public synchronized int activitySize() {
        return sStack.size();
    }

    /**
     * 打印信息
     */
    public synchronized void printActStack() {
        for (int i = 0; i < sStack.size(); i++) {
            Logger.i("activity-->" + sStack.get(i).getClass().getSimpleName());
        }
    }
}
