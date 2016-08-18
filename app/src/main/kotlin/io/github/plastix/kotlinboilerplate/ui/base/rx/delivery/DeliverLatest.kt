package io.github.plastix.kotlinboilerplate.ui.base.rx.delivery

import rx.Observable

/**
 * {@link rx.Observable.Transformer} that couples data and view status
 *
 * If view is attached (latest emitted value from view observable is true) then values from data
 * observable propagates us usual.
 *
 * If view is detached (latest emitted value from view observable is false) then values from data
 * observable propagates using following rules:
 * <ul>
 *     <li>If data observable emits onError then it would be delivered after view is attached</li>
 *     <li>If data observable emits onCompleted then after view is attached last onNext value from
 *     data observable is delivered followed by onCompleted event</li>
 *     <li>If data observable emits multiple values then after view is attached last emitted value
 *     is delivered</li>
 * </ul>
 *
 * Adapted from https://github.com/alapshin/arctor (MIT License)
 */
class DeliverLatest<T>(private val view: Observable<Boolean>) : Observable.Transformer<T, T> {

    override fun call(observable: Observable<T>): Observable<T> {
        return Observable.combineLatest(
                view,
                // Materialize data Observable to handle onError and onCompleted events when view is detached
                observable.materialize()
                        // Keep the last two notifications
                        .buffer(2, 1)
                        .delay { notifications ->
                            if (notifications.last().isOnCompleted) {
                                view.filter { value -> value }
                            } else {
                                Observable.just(true)
                            }
                        }
                        // If the last received notification is onCompleted then delay
                        // emission of previous onNext and onCompleted notification until view is attached
                        .scan { notifications, notifications2 ->
                            if (notifications == null) {
                                // If it is first buffer of notifications emit it as usual
                                notifications2
                            } else {
                                // Otherwise remove first element from buffer since it is
                                // already emitted as last element of the previous buffer
                                notifications2.subList(1, notifications2.size)
                            }
                        } // Remove duplicate notifications caused by sliding buffer
                        .flatMap { notifications -> Observable.from(notifications) }
                // Flatten emitted buffers
        ) { flag, notification -> if (flag) notification else null }
                .filter { it != null }
                .dematerialize<T>()
    }
}