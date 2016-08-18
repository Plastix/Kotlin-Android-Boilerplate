package io.github.plastix.kotlinboilerplate.ui.list

import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.ui.base.MvpView

interface ListView : MvpView {

    fun updateList(repos: List<Repo>)

    fun startLoading()

    fun stopLoading()

    fun errorNoNetwork()

    fun errorFetchRepos()

}