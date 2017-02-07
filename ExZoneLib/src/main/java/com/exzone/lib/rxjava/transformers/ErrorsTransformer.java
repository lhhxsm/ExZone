package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;

import rx.Notification;
import rx.Observable;
import rx.functions.Func1;

public final class ErrorsTransformer<T> implements Observable.Transformer<Notification<T>, Throwable> {

    @Override
    public
    @NonNull
    Observable<Throwable> call(final @NonNull Observable<Notification<T>> source) {
        return source
                .filter(new Func1<Notification<T>, Boolean>() {
                    @Override
                    public Boolean call(Notification<T> tNotification) {
                        return tNotification.hasThrowable();
                    }
                })
                .map(new Func1<Notification<T>, Throwable>() {
                    @Override
                    public Throwable call(Notification<T> tNotification) {
                        return tNotification.getThrowable();
                    }
                });
    }
}

