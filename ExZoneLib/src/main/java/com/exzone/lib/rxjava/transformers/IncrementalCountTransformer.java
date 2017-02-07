package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.functions.Func2;

public final class IncrementalCountTransformer<T> implements Observable.Transformer<T, Integer> {
    final int firstPage;

    public IncrementalCountTransformer() {
        firstPage = 1;
    }

    public IncrementalCountTransformer(final int firstPage) {
        this.firstPage = firstPage;
    }

    @Override
    public Observable<Integer> call(final @NonNull Observable<T> source) {
        return source.scan(firstPage - 1, new Func2<Integer, T, Integer>() {
            @Override
            public Integer call(Integer integer, T t) {
                return integer + 1;
            }
        }).skip(1);
    }
}
