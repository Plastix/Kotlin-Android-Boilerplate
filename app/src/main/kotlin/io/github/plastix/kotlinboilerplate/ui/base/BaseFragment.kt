package io.github.plastix.kotlinboilerplate.ui.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import io.github.plastix.kotlinboilerplate.ApplicationComponent
import io.github.plastix.kotlinboilerplate.KotlinBoilerplateApp

abstract class BaseFragment : Fragment() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies(KotlinBoilerplateApp.graph)
    }

    abstract fun injectDependencies(graph: ApplicationComponent)

}