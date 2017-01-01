package io.github.plastix.kotlinboilerplate.ui.base

interface ViewModel {

    fun bind()

    fun unbind()

    fun onDestroy()
}