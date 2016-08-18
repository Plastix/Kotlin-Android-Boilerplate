package io.github.plastix.kotlinboilerplate.ui.detail

import dagger.Subcomponent
import io.github.plastix.kotlinboilerplate.ui.ActivityScope

@ActivityScope
@Subcomponent(modules = arrayOf(
        DetailModule::class
))
interface DetailComponent {
    fun injectTo(activity: DetailActivity)
}