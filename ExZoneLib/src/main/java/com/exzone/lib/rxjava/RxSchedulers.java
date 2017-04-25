package com.exzone.lib.rxjava;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述:RxJava调度管理
 * 作者:李鸿浩
 * 时间:2016/11/21.
 */

public class RxSchedulers {
  public static <T> Observable.Transformer<T, T> ioMain() {
    return new Observable.Transformer<T, T>() {
      @Override public Observable<T> call(Observable<T> tObservable) {
        return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
      }
    };
  }

  public static <T> Observable.Transformer<T, T> newThread() {
    return new Observable.Transformer<T, T>() {
      @Override public Observable<T> call(Observable<T> tObservable) {
        return tObservable.observeOn(Schedulers.io())//开启新的线程
            .observeOn(AndroidSchedulers.mainThread());//切换到UI线程
      }
    };
  }

  public static <T> Observable.Transformer<T, T> defaultSchedulers() {
    return new Observable.Transformer<T, T>() {
      @Override public Observable<T> call(Observable<T> tObservable) {
        return tObservable.unsubscribeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
      }
    };
  }
}
