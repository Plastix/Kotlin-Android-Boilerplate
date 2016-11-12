package io.github.plastix.kotlinboilerplate.ui.base

import android.content.Context
import android.support.v4.app.Fragment
import dagger.Module
import dagger.Provides

@Module
abstract class FragmentModule(private val fragment: Fragment) {

    @Provides
    fun provideFragment(): Fragment = fragment

    @Provides
    fun provideFragmentContext(): Context = fragment.context
}