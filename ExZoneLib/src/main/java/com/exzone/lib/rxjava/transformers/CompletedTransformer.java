package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;

import rx.Notification;
import rx.Observable;
import rx.functions.Func1;

public final class CompletedTransformer<T> implements Observable.Transformer<Notification<T>, Void> {

    @Override
    public
    @NonNull
    Observable<Void> call(final @NonNull Observable<Notification<T>> source) {
        return source
                .filter(new Func1<Notification<T>, Boolean>() {
                    @Override
                    public Boolean call(Notification<T> tNotification) {
                        return tNotification.isOnCompleted();
                    }
                })
                .map(new Func1<Notification<T>, Void>() {
                    @Override
                    public Void call(Notification<T> tNotification) {
                        return null;
                    }
                });
    }
}

