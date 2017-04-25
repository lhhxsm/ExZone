package com.exzone.lib.util;

/**
 * 作者:李鸿浩
 * 描述:单例模式工具类
 * 时间:2016/7/9.
 */
public abstract class SingletonUtils<T> {
  private T instance;

  protected abstract T newInstance();

  public final T getInstance() {
    if (instance == null) {
      synchronized (SingletonUtils.class) {
        if (instance == null) {
          instance = newInstance();
        }
      }
    }
    return instance;
  }
}
