package io.github.plastix.kotlinboilerplate.ui.base.rx.delivery

import rx.Observable
import rx.subjects.ReplaySubject

/**
 * Adapted from https://github.com/alapshin/arctor (MIT License)
 */
class DeliverReplay<T>(private val view: Observable<Boolean>) : Observable.Transformer<T, T> {

    override fun call(observable: Observable<T>): Observable<T> {
        val subject = ReplaySubject.create<T>()
        val subscription = observable.subscribe(subject)
        return view.switchMap { flag -> if (flag) subject else Observable.empty<T>() }
                .doOnUnsubscribe { subscription.unsubscribe() }
    }
}