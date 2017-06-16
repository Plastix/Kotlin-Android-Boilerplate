package io.github.plastix.kotlinboilerplate.ui.base

import activitystarter.ActivityStarter
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import io.github.plastix.kotlinboilerplate.ApplicationComponent
import io.github.plastix.kotlinboilerplate.KotlinBoilerplateApp

abstract class BaseActivity : AppCompatActivity() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStarter.fill(this)
        injectDependencies(KotlinBoilerplateApp.graph)
    }

    abstract fun injectDependencies(graph: ApplicationComponent)
}