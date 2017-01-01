package io.github.plastix.kotlinboilerplate.ui.detail

import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.ui.base.ActivityModule

@Module
class DetailModule(activity: AppCompatActivity, val repo: Repo) : ActivityModule(activity) {

    @Provides
    fun provideRepo(): Repo = repo
}