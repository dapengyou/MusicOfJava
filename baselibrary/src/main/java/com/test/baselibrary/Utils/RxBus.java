package com.test.baselibrary.Utils;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by wanghongjia on 2017/11/9.
 */

public class RxBus {

    private static volatile RxBus instance;
    private final Subject<Object, Object> _bus;


    private RxBus() {
        _bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance() {
        if (null == instance) {
            synchronized (RxBus.class) {
                if (null == instance) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    public void send(Object object) {
        try {
            _bus.onNext(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }


    private <T> Observable<T> toObservable(final Class<T> type) {
        return _bus.ofType(type);//filter + cast
    }


    public <T> Subscription toSubscription(final Class<T> type, Observer<T> observer) {
        return toObservable(type).subscribe(observer);
    }

    public <T> Subscription toSubscription(final Class<T> type, Action1<T> action1) {
        return toObservable(type).subscribe(action1);
    }

    public <T> Subscription toSubscription(final Class<T> type, Action1<T> action1, Action1<Throwable> errorAction1) {
        return toObservable(type).subscribe(action1, errorAction1);
    }
}

