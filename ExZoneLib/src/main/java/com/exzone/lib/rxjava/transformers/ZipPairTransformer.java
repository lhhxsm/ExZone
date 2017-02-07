package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;
import android.util.Pair;

import rx.Observable;
import rx.functions.Func2;

public final class ZipPairTransformer<T, R> implements Observable.Transformer<T, Pair<T, R>> {
    @NonNull
    private final Observable<R> second;

    public ZipPairTransformer(final @NonNull Observable<R> second) {
        this.second = second;
    }

    @Override
    @NonNull
    public Observable<Pair<T, R>> call(final @NonNull Observable<T> first) {
        return Observable.zip(first, second, new Func2<T, R, Pair<T, R>>() {
            @Override
            public Pair<T, R> call(T t, R r) {
                return new Pair<T, R>(t, r);
            }
        });
    }
}
