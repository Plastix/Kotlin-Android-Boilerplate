package io.github.plastix.kotlinboilerplate.ui.base.rx.delivery

import rx.Observable
import rx.subjects.ReplaySubject
import rx.subscriptions.Subscriptions

/**
 * Adapted from https://github.com/alapshin/arctor (MIT License)
 */
class DeliverReplay<T>(private val view: Observable<Boolean>) : Observable.Transformer<T, T> {

    override fun call(observable: Observable<T>): Observable<T> {
        val subject = ReplaySubject.create<T>()
        var subscription = Subscriptions.unsubscribed()
        return view.switchMap {
            flag ->
            if (flag) subject else Observable.never<T>()
        }.doOnSubscribe {
            subscription = observable.subscribe(subject)
        }.doOnUnsubscribe {
            subscription.unsubscribe()
        }
    }
}