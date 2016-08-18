package io.github.plastix.kotlinboilerplate.ui.list

import dagger.Subcomponent
import io.github.plastix.kotlinboilerplate.ui.ActivityScope

@ActivityScope
@Subcomponent(modules = arrayOf(
        ListModule::class
))
interface ListComponent {

    fun injectTo(activity: ListActivity)
}