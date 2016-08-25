package io.github.plastix.kotlinboilerplate.ui.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import javax.inject.Inject
import javax.inject.Provider

abstract class PresenterActivity<V : MvpView, T : Presenter<V>> : BaseActivity(),
        LoaderManager.LoaderCallbacks<T> {

    private val LOADER_ID = 1
    protected lateinit var presenter: T

    @Inject
    protected lateinit var presenterLoaderProvider: Provider<PresenterLoader<T>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLoader()
    }

    @CallSuper
    protected fun onPresenterProvided(presenter: T) {
        this.presenter = presenter
    }

    @CallSuper
    protected fun onPresenterDestroyed() {
        // Hook for subclasses
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        presenter.bindView(getViewLayer())
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    protected fun getViewLayer(): V {
        @Suppress("UNCHECKED_CAST")
        return this as V
    }


    private fun initLoader() {
        supportLoaderManager.initLoader<T>(LOADER_ID, null, this)
    }

    override fun onLoadFinished(loader: Loader<T>?, presenter: T) {
        onPresenterProvided(presenter)
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<T> {
        return presenterLoaderProvider.get()
    }

    override fun onLoaderReset(loader: Loader<T>?) {
        onPresenterDestroyed()
    }

}