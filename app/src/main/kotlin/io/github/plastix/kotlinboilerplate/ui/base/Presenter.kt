package io.github.plastix.kotlinboilerplate.ui.base

interface Presenter<in V : MvpView> {

    fun bindView(view: V)

    fun unbindView()

    fun onDestroy()
}