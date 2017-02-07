package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;
import android.util.Pair;

import rx.Observable;
import rx.functions.Func2;

public final class CombineLatestPairTransformer<S, T> implements Observable.Transformer<S, Pair<S, T>> {
    @NonNull
    private final Observable<T> second;

    public CombineLatestPairTransformer(final @NonNull Observable<T> second) {
        this.second = second;
    }

    @Override
    @NonNull
    public Observable<Pair<S, T>> call(final @NonNull Observable<S> first) {
        return Observable.combineLatest(first, second, new Func2<S, T, Pair<S, T>>() {
            @Override
            public Pair<S, T> call(S s, T t) {
                return new Pair<S, T>(s, t);
            }
        });
    }
}
