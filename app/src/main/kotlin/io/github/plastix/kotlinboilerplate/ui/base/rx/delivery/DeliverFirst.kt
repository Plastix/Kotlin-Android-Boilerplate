package io.github.plastix.kotlinboilerplate.ui.base.rx.delivery

import rx.Observable

/**
 * Transformer which couples data Observable with view Observable.
 *
 * Adapted from https://github.com/alapshin/arctor (MIT License)
 */
class DeliverFirst<T>(private val view: Observable<Boolean>) : Observable.Transformer<T, T> {

    override fun call(observable: Observable<T>): Observable<T> {
        return Observable.combineLatest(
                view,
                // Emit only first value from data Observable
                observable.first()
                        // Use materialize to propagate onError events from data Observable
                        // only after view Observable emits true
                        .materialize()
                        .delay { view.filter { value -> value } }
        ) { flag, notification -> if (flag) notification else null }
                .filter { it != null }
                .dematerialize<T>()
    }
}
