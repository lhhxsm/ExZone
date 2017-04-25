package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;
import rx.Observable;
import rx.functions.Func1;

public final class WaitUntilTransformer<T, R> implements Observable.Transformer<T, T> {
  @NonNull private final Observable<R> until;

  public WaitUntilTransformer(final @NonNull Observable<R> until) {
    this.until = until;
  }

  @Override public Observable<T> call(final @NonNull Observable<T> source) {
    return until.take(1).flatMap(new Func1<R, Observable<T>>() {
      @Override public Observable<T> call(R r) {
        return source;
      }
    });
  }
}
