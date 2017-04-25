package com.exzone.lib.rxjava.transformers;

import android.os.Looper;
import android.support.annotation.NonNull;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class ObserveForUITransformer<T> implements Observable.Transformer<T, T> {
  @Override public @NonNull Observable<T> call(final @NonNull Observable<T> source) {
    return source.flatMap(new Func1<T, Observable<T>>() {
      @Override public Observable<T> call(T t) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
          return Observable.just(t).observeOn(Schedulers.immediate());
        } else {
          return Observable.just(t).observeOn(AndroidSchedulers.mainThread());
        }
      }
    });
  }
}
