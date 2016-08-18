package io.github.plastix.kotlinboilerplate.ui.list

import io.github.plastix.kotlinboilerplate.data.network.NetworkInteractor
import io.github.plastix.kotlinboilerplate.data.remote.ApiConstants
import io.github.plastix.kotlinboilerplate.data.remote.GithubApiService
import io.github.plastix.kotlinboilerplate.data.remote.model.SearchResponse
import io.github.plastix.kotlinboilerplate.ui.base.rx.RxPresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import javax.inject.Inject

class ListPresenterImpl @Inject constructor(private val apiService: GithubApiService,
                                            private val networkInteractor: NetworkInteractor) : RxPresenter<ListView>(), ListPresenter {

    private var subscription: Subscription = Subscriptions.unsubscribed()

    override fun getKotlinRepos() {
        subscription.unsubscribe() // Unsubscribe from any current running request

        view?.startLoading()

        subscription = networkInteractor.hasNetworkConnectionCompletable()
                .andThen(apiService.repoSearch(ApiConstants.SEARCH_QUERY_KOTLIN,
                        ApiConstants.SEARCH_SORT_STARS,
                        ApiConstants.SEARCH_ORDER_DESCENDING))
                .toObservable()
                .compose(deliverFirst<SearchResponse>())
                .toSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.updateList(it.repos)
                }) {
                    when (it) {
                        is NetworkInteractor.NetworkUnavailableException -> view?.errorNoNetwork()
                        else -> view?.errorFetchRepos()
                    }
                }
        addSubscription(subscription)

    }

    override fun bindView(view: ListView) {
        super.bindView(view)

        // If we have a currently running subscription it means we should set the view to loading
        if (!subscription.isUnsubscribed) {
            view.startLoading()
        }
    }
}