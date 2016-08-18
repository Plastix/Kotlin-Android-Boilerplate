package io.github.plastix.kotlinboilerplate.ui.base

abstract class AbstractPresenter<V : MvpView> : Presenter<V> {

    protected var view: V? = null

    override fun bindView(view: V) {
        this.view = view
    }

    override fun unbindView() {
        this.view = null
    }


    override fun onDestroy() {
        // Hook for subclasses to clean up used resources
    }
}