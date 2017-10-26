package com.exzone.lib.rxjava;

import android.support.annotation.NonNull;
import com.exzone.lib.util.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * 描述:用RxJava实现事件总线功能
 * 作者:lhh
 * 时间:2016/11/21.
 */

public class RxBus {
  private static RxBus instance;
  private ConcurrentHashMap<Object, List<Subject>> mSubject = new ConcurrentHashMap<>();

  private RxBus() {
  }

  public static synchronized RxBus getInstance() {
    if (null == instance) {
      instance = new RxBus();
    }
    return instance;
  }

  public static boolean isEmpty(Collection<Subject> collection) {
    return null == collection || collection.isEmpty();
  }

  /**
   * 订阅事件源
   */
  public RxBus onEvent(Observable<?> observable, Action1<Object> action1) {
    observable.observeOn(AndroidSchedulers.mainThread())
        .subscribe(action1, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            throwable.printStackTrace();
          }
        });
    return getInstance();
  }

  /**
   * 注册事件源
   */
  public <T> Observable<T> register(@NonNull Object tag) {
    List<Subject> list = mSubject.get(tag);
    if (null == list) {
      list = new ArrayList<>();
      mSubject.put(tag, list);
    }
    Subject<T, T> subject;
    list.add(subject = PublishSubject.create());
    Logger.e("register-->" + tag + "  size:" + list.size());
    return subject;
  }

  public void unRegister(@NonNull Object tag) {
    List<Subject> list = mSubject.get(tag);
    if (null != list) {
      mSubject.remove(tag);
    }
  }

  /**
   * 取消注册
   */
  public RxBus unRegister(@NonNull Object tag, @NonNull Observable<?> observable) {
    List<Subject> list = mSubject.get(tag);
    if (list != null) {
      list.remove(observable);
      if (isEmpty(list)) {
        mSubject.remove(tag);
        Logger.e("unRegister-->" + tag + "  size:" + list.size());
      }
    }
    return getInstance();
  }

  public void post(@NonNull Object content) {
    post(content.getClass().getName(), content);
  }

  /**
   * 触发事件
   */
  @SuppressWarnings("unchecked") public void post(@NonNull Object tag, @NonNull Object content) {
    Logger.e("post-->eventName: " + tag);
    List<Subject> list = mSubject.get(tag);
    if (!isEmpty(list)) {
      for (Subject subject : list) {
        subject.onNext(content);
        Logger.e("post-->eventName: " + tag);
      }
    }
  }
}
