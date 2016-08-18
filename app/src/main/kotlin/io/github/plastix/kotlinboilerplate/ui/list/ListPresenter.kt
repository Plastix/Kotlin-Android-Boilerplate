package io.github.plastix.kotlinboilerplate.ui.list

import io.github.plastix.kotlinboilerplate.ui.base.Presenter

interface ListPresenter : Presenter<ListView>{

    fun getKotlinRepos()

}