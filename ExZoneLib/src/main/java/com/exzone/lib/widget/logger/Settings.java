package com.exzone.lib.widget.logger;

/**
 * 作者:李鸿浩
 * 描述: 打印日志设置类
 * 时间:2016/7/9.
 */
public final class Settings {
    /**
     * 是否打印日志
     */
    private boolean isDebug = true;
    private int methodCount = 2;
    private int methodOffset = 0;
    private boolean showThreadInfo = true;
    private LogAdapter logAdapter;

    public boolean isDebug() {
        return isDebug;
    }

    public Settings setDebug(boolean debug) {
        isDebug = debug;
        return this;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public Settings setMethodCount(int methodCount) {
        this.methodCount = methodCount;
        return this;
    }

    public int getMethodOffset() {
        if (methodCount < 0) {
            methodCount = 0;
        }
        return methodOffset;
    }

    public Settings setMethodOffset(int methodOffset) {
        this.methodOffset = methodOffset;
        return this;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }

    public Settings setShowThreadInfo(boolean showThreadInfo) {
        this.showThreadInfo = showThreadInfo;
        return this;
    }

    public LogAdapter getLogAdapter() {
        if (logAdapter == null) {
            logAdapter = new AndroidLogAdapter();
        }
        return logAdapter;
    }

    public Settings setLogAdapter(LogAdapter logAdapter) {
        this.logAdapter = logAdapter;
        return this;
    }

    public void reset() {
        methodCount = 2;
        methodOffset = 0;
        showThreadInfo = true;
        isDebug = true;
    }
}
