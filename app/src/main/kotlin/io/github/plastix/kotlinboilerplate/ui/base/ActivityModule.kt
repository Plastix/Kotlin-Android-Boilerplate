package io.github.plastix.kotlinboilerplate.ui.base

import android.content.Context
import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
abstract class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    fun provideActivity(): AppCompatActivity = activity

    @Provides
    fun provideActivityContext(): Context = activity.baseContext
}