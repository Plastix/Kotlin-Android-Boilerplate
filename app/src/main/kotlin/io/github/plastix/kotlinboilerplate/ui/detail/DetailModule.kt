package io.github.plastix.kotlinboilerplate.ui.detail

import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import io.github.plastix.kotlinboilerplate.ui.ActivityScope
import io.github.plastix.kotlinboilerplate.ui.base.ActivityModule

@Module
class DetailModule(activity: AppCompatActivity) : ActivityModule(activity) {

    @Provides @ActivityScope
    fun providePresenter(presenter: DetailPresenterImpl): DetailPresenter = presenter
}