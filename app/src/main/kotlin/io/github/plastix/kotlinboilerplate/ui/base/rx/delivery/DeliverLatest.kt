package io.github.plastix.kotlinboilerplate.ui.base.rx.delivery

import rx.Observable
import rx.lang.kotlin.filterNotNull

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
                        .delay { notification ->
                            // Delay completed notifications until the view reattaches
                            if (notification.isOnCompleted) {
                                view.first { it }
                            } else {
                                // Pass all other events downstream immediately
                                // They will be "cached" by combineLatest
                                Observable.empty()
                            }
                        }
        ) {
            // Pass notification downstream if view is attached, otherwise null
            isViewAttached, notification ->
            if (isViewAttached) notification else null
        }
                //  Filter out null events to ensure we only emit when the view is attached
                .filterNotNull()
                // Convert our notifications back into values
                .dematerialize()
    }
}