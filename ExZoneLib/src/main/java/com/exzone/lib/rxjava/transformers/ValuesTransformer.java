package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;

import rx.Notification;
import rx.Observable;
import rx.functions.Func1;

public final class ValuesTransformer<T> implements Observable.Transformer<Notification<T>, T> {

    @Override
    public
    @NonNull
    Observable<T> call(final @NonNull Observable<Notification<T>> source) {
        return source
                .filter(new Func1<Notification<T>, Boolean>() {
                    @Override
                    public Boolean call(Notification<T> tNotification) {
                        return tNotification.isOnNext();
                    }
                })
                .map(new Func1<Notification<T>, T>() {
                    @Override
                    public T call(Notification<T> tNotification) {
                        return tNotification.getValue();
                    }
                });
    }
}

