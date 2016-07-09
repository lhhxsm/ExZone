package com.exzone.lib.widget.logger;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/9.
 */
public interface LogAdapter {
    void v(String tag, String message);

    void d(String tag, String message);

    void i(String tag, String message);

    void w(String tag, String message);

    void e(String tag, String message);

    void wtf(String tag, String message);
}