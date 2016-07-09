package com.exzone.lib.widget.logger;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/9.
 */
public interface Printer {

    Settings init(String tag);

    Settings getSettings();

    Printer t(String tag, int methodCount);

    void v(String message, Object... args);

    void d(String message, Object... args);

    void d(Object object);

    void i(String message, Object... args);

    void w(String message, Object... args);

    void e(String message, Object... args);

    void e(Throwable throwable, String message, Object... args);

    void wtf(String message, Object... args);

    void json(String json);

    void xml(String xml);

    void log(int priority, String tag, String message, Throwable throwable);

    void resetSettings();
}
