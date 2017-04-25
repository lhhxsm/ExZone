package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;
import android.util.Pair;
import rx.Observable;
import rx.functions.Func2;

public final class TakePairWhenTransformer<S, T> implements Observable.Transformer<S, Pair<S, T>> {
  @NonNull private final Observable<T> when;

  public TakePairWhenTransformer(final @NonNull Observable<T> when) {
    this.when = when;
  }

  @Override @NonNull public Observable<Pair<S, T>> call(final @NonNull Observable<S> source) {
    return when.withLatestFrom(source, new Func2<T, S, Pair<S, T>>() {
      @Override public Pair<S, T> call(T t, S s) {
        return new Pair<S, T>(s, t);
      }
    });
  }
}
