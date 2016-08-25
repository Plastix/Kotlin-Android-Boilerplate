package io.github.plastix.kotlinboilerplate.ui.base.rx.delivery

import rx.Observable
import rx.lang.kotlin.filterNotNull

/**
 * Transformer which couples data Observable with view Observable.
 *
 * Adapted from https://github.com/alapshin/arctor (MIT License)
 */
class DeliverFirst<T>(private val view: Observable<Boolean>) : Observable.Transformer<T, T> {

    override fun call(observable: Observable<T>): Observable<T> {

        // This is nearly identical to DeliverLatest except we call take(1) on the data observable first
        return Observable.combineLatest(
                view,
                // Emit only first value from data Observable
                observable.take(1)
                        // Use materialize to propagate onError events from data Observable
                        // only after view Observable emits true
                        .materialize()
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
        ) { flag, notification ->
            if (flag) notification else null
        }
                .filterNotNull()
                .dematerialize()
    }
}
