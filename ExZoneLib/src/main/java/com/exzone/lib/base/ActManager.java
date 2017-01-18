package com.exzone.lib.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * 功能:Activity管理类
 * 作者:李鸿浩
 * 时间:2016/11/17.
 */
public class ActManager {
    private static Stack<Activity> sActivities;

    private volatile static ActManager sInstance;

    private ActManager() {
    }

    /**
     * 单例模式
     */
    public static ActManager getInstance() {
        if (sInstance == null) {
            synchronized (ActManager.class){
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
        if (sActivities == null) {
            sActivities = new Stack<Activity>();
        }
        sActivities.add(activity);
    }

    /**
     * 获取当前的Activity,即堆栈中的最后一个
     */
    public Activity currentActivity() {
        Activity activity = sActivities.lastElement();
        return activity;
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            sActivities.remove(activity);
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
        for (Activity activity : sActivities) {
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
        for (int i = 0, size = sActivities.size(); i < size; i++) {
            if (null != sActivities.get(i)) {
                sActivities.get(i).finish();
            }
        }
        sActivities.clear();
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
}
