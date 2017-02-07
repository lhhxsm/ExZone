package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.functions.Func1;

public final class IgnoreValuesTransformer<S> implements Observable.Transformer<S, Void> {
    @Override
    @NonNull
    public Observable<Void> call(final @NonNull Observable<S> source) {
        return source.map(new Func1<S, Void>() {
            @Override
            public Void call(S s) {
                return null;
            }
        });
    }
}
