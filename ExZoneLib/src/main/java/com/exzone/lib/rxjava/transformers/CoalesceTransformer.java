package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;
import rx.Observable;
import rx.functions.Func1;

/**
 * 合并变换
 */
public final class CoalesceTransformer<T> implements Observable.Transformer<T, T> {
  private final T theDefault;

  public CoalesceTransformer(final @NonNull T theDefault) {
    this.theDefault = theDefault;
  }

  @Override public @NonNull Observable<T> call(final @NonNull Observable<T> source) {
    return source.map(new Func1<T, T>() {
      @Override public T call(T t) {
        if (t != null) {
          return t;
        } else {
          return theDefault;
        }
      }
    });
  }
}
