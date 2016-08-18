package io.github.plastix.kotlinboilerplate.ui.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import javax.inject.Inject
import javax.inject.Provider

abstract class PresenterFragment<V : MvpView, T : Presenter<V>> : BaseFragment(),
        LoaderManager.LoaderCallbacks<T> {

    private val LOADER_ID = 1

    protected lateinit var presenter: T

    // Boolean flag to avoid delivering the Presenter twice. Calling initLoader in onActivityCreated means
    // onLoadFinished will be called twice during configuration change.
    private var delivered = false

    @Inject
    protected lateinit var presenterLoaderProvider: Provider<PresenterLoader<T>>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initLoader()
    }

    private fun initLoader() {
        loaderManager.initLoader(LOADER_ID, null, this)
    }

    @CallSuper
    protected fun onPresenterProvided(presenter: T) {
        this.presenter = presenter
    }

    @CallSuper
    protected fun onPresenterDestroyed() {
        presenter.onDestroy()
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

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        if (activity.isFinishing) {
            presenter.onDestroy()
        }
    }

    override fun onLoadFinished(loader: Loader<T>?, presenter: T) {
        if (!delivered) {
            onPresenterProvided(presenter)
            delivered = true
        }
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<T> {
        return presenterLoaderProvider.get()
    }

    override fun onLoaderReset(loader: Loader<T>?) {
        onPresenterDestroyed()
    }
}