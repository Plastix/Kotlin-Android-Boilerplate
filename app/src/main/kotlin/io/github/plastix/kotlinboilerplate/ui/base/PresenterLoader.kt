package io.github.plastix.kotlinboilerplate.ui.base

import android.content.Context
import android.support.v4.content.Loader
import io.github.plastix.kotlinboilerplate.ui.ActivityScope
import javax.inject.Inject
import javax.inject.Provider

class PresenterLoader<T : Presenter<*>> @Inject constructor(@ActivityScope context: Context,
                                                            private val presenterFactory: Provider<T>) : Loader<T>(context) {
    private var presenter: T? = null

    override fun onStartLoading() {
        super.onStartLoading()

        if (presenter == null)
            forceLoad()
        else
            deliverResult(presenter)
    }

    override fun onForceLoad() {
        super.onForceLoad()
        presenter = presenterFactory.get()

        deliverResult(presenter)
    }

    override fun onReset() {
        super.onReset()

        presenter?.onDestroy()
    }
}