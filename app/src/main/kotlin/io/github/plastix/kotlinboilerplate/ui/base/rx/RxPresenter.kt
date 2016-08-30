package io.github.plastix.kotlinboilerplate.ui.base.rx

import android.support.annotation.CallSuper
import io.github.plastix.kotlinboilerplate.ui.base.AbstractPresenter
import io.github.plastix.kotlinboilerplate.ui.base.MvpView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

abstract class RxPresenter<V : MvpView> : AbstractPresenter<V>() {

    protected val disposables: CompositeDisposable = CompositeDisposable()
    private val viewState: BehaviorSubject<Boolean> = BehaviorSubject.create()

    init {
        viewState.onNext(false)
    }

    @CallSuper
    override fun bindView(view: V) {
        super.bindView(view)
        viewState.onNext(true)
    }

    @CallSuper
    override fun unbindView() {
        super.unbindView()
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