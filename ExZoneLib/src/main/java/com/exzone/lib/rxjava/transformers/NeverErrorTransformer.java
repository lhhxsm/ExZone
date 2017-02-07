package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.functions.Action1;

public final class NeverErrorTransformer<T> implements Observable.Transformer<T, T> {
    private final
    @Nullable
    Action1<Throwable> errorAction;

    protected NeverErrorTransformer() {
        this.errorAction = null;
    }

    protected NeverErrorTransformer(final @Nullable Action1<Throwable> errorAction) {
        this.errorAction = errorAction;
    }

    @Override
    @NonNull
    public Observable<T> call(final @NonNull Observable<T> source) {
        return source.doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (errorAction != null) {
                    errorAction.call(throwable);
                }
            }
        }).onErrorResumeNext(Observable.<T>empty());
    }
}
