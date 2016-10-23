package com.exzone.lib.okhttp.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.exzone.lib.util.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/23.
 */
public class Platform {
    private static final Platform PLATFORM = findPlatform();

    public static Platform getPlatform() {
        Logger.e(PLATFORM.getClass().toString());
        return PLATFORM;
    }

    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Platform();
    }

    public Executor defaultCallbackExecutor() {
        return Executors.newCachedThreadPool();
    }

    public void execute(Runnable runnable) {
        defaultCallbackExecutor().execute(runnable);
    }

    static class Android extends Platform {
        @Override
        public Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        static class MainThreadExecutor implements Executor {
            private final Handler mHandler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(Runnable runnable) {
                mHandler.post(runnable);
            }
        }
    }
}
