package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.functions.Func2;

public final class TakeWhenTransformer<S, T> implements Observable.Transformer<S, S> {
    @NonNull
    private final Observable<T> when;

    public TakeWhenTransformer(final @NonNull Observable<T> when) {
        this.when = when;
    }

    @Override
    @NonNull
    public Observable<S> call(final @NonNull Observable<S> source) {
        return when.withLatestFrom(source, new Func2<T, S, S>() {
            @Override
            public S call(T t, S s) {
                return s;
            }
        });
    }
}
