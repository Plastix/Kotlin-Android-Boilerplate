package io.github.plastix.kotlinboilerplate.ui.list

import io.github.plastix.kotlinboilerplate.data.network.NetworkInteractor
import io.github.plastix.kotlinboilerplate.data.remote.ApiConstants
import io.github.plastix.kotlinboilerplate.data.remote.GithubApiService
import io.github.plastix.kotlinboilerplate.data.remote.model.SearchResponse
import io.github.plastix.kotlinboilerplate.ui.base.rx.RxPresenter
import io.github.plastix.rxdelay.RxDelay
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListPresenterImpl @Inject constructor(private val apiService: GithubApiService,
                                            private val networkInteractor: NetworkInteractor) : RxPresenter<ListView>(), ListPresenter {

    private var networkRequest: Disposable = Disposables.disposed()

    override fun getKotlinRepos() {
        networkInteractor.hasNetworkConnectionCompletable()
                .andThen(apiService.repoSearch(ApiConstants.SEARCH_QUERY_KOTLIN,
                        ApiConstants.SEARCH_SORT_STARS,
                        ApiConstants.SEARCH_ORDER_DESCENDING))
                .subscribeOn(Schedulers.io())
                .compose(RxDelay.delaySingle(getViewState()))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    networkRequest.dispose() // Cancel any current running request
                    view?.startLoading() // Set the view's progress indicator
                }
                .subscribe(object : SingleObserver<SearchResponse> {
                    override fun onSuccess(value: SearchResponse) {
                        view?.updateList(value.repos)
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is NetworkInteractor.NetworkUnavailableException -> view?.errorNoNetwork()
                            else -> view?.errorFetchRepos()
                        }
                    }

                    override fun onSubscribe(disposable: Disposable) {
                        addDisposable(disposable)
                        networkRequest = disposable
                    }
                })
    }

    override fun bindView(view: ListView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!networkRequest.isDisposed) {
            view.startLoading()
        }
    }
}