package io.github.plastix.kotlinboilerplate.ui.detail

import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.ui.base.AbstractViewModel
import javax.inject.Inject

class DetailViewModel @Inject constructor(val repo: Repo) : AbstractViewModel() {

    fun getName() = repo.fullName

    fun getDescription() = repo.description

    fun getStars() = repo.stars.toString()

    fun getForks() = repo.forks.toString()

    fun getAvatarURL() = repo.owner.avatarUrl

}