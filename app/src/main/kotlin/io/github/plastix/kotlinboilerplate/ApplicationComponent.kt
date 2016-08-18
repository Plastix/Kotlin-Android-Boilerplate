package io.github.plastix.kotlinboilerplate

import dagger.Component
import io.github.plastix.kotlinboilerplate.data.network.NetworkModule
import io.github.plastix.kotlinboilerplate.data.remote.ApiModule
import io.github.plastix.kotlinboilerplate.ui.detail.DetailComponent
import io.github.plastix.kotlinboilerplate.ui.detail.DetailModule
import io.github.plastix.kotlinboilerplate.ui.list.ListComponent
import io.github.plastix.kotlinboilerplate.ui.list.ListModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        NetworkModule::class,
        ApiModule::class
))
interface ApplicationComponent {

    // Injectors
    fun injectTo(app: KotlinBoilerplateApp)

    // Submodule methods
    // Every screen is its own submodule of the graph and must be added here.
    fun plus(module: ListModule): ListComponent
    fun plus(module: DetailModule): DetailComponent
}