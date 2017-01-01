package io.github.plastix.kotlinboilerplate.ui.base

import android.support.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

abstract class RxViewModel : AbstractViewModel() {

    protected val disposables: CompositeDisposable = CompositeDisposable()
    private val viewState: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    @CallSuper
    override fun bind() {
        super.bind()
        viewState.onNext(true)
    }

    @CallSuper
    override fun unbind() {
        super.unbind()
        viewState.onNext(false)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        viewState.onComplete()
        clearSubscriptions()
    }

    private fun clearSubscriptions() {
        disposables.clear()
    }

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    /**
     * Returns an Observable which omits the current state of the view. This observable emits
     * true when the view is attached and false when it is detached.
     */
    fun getViewState(): Observable<Boolean> = viewState.hide()

}