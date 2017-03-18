package io.github.plastix.kotlinboilerplate.ui.list

import io.github.plastix.kotlinboilerplate.data.network.NetworkInteractor
import io.github.plastix.kotlinboilerplate.data.remote.ApiConstants
import io.github.plastix.kotlinboilerplate.data.remote.GithubApiService
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.data.remote.model.SearchResponse
import io.github.plastix.kotlinboilerplate.ui.base.RxViewModel
import io.github.plastix.rxdelay.RxDelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ListViewModel @Inject constructor(
        private val apiService: GithubApiService,
        private val networkInteractor: NetworkInteractor
) : RxViewModel() {

    private var networkRequest: Disposable = Disposables.disposed()

    private var repos: BehaviorSubject<List<Repo>> = BehaviorSubject.createDefault(emptyList())
    private var loadingState: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val fetchErrors: PublishSubject<Throwable> = PublishSubject.create()
    private val networkErrors: PublishSubject<Throwable> = PublishSubject.create()

    fun fetchRepos() {
        networkRequest = networkInteractor.hasNetworkConnectionCompletable()
                .andThen(apiService.repoSearch(ApiConstants.SEARCH_QUERY_KOTLIN,
                        ApiConstants.SEARCH_SORT_STARS,
                        ApiConstants.SEARCH_ORDER_DESCENDING))
                .subscribeOn(Schedulers.io())
                .compose(RxDelay.delaySingle(getViewState()))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    networkRequest.dispose() // Cancel any current running request
                    loadingState.onNext(true)
                }
                .doOnEvent { searchResponse, throwable ->
                    loadingState.onNext(false)
                }
                .subscribeWith(object : DisposableSingleObserver<SearchResponse>() {
                    override fun onError(e: Throwable) {
                        System.out.println(e.toString())
                        when (e) {
                            is NetworkInteractor.NetworkUnavailableException -> networkErrors.onNext(e)
                            else -> fetchErrors.onNext(e)
                        }
                    }

                    override fun onSuccess(value: SearchResponse) {
                        repos.onNext(value.repos)
                    }
                })

        addDisposable(networkRequest)
    }

    fun getRepos(): Observable<List<Repo>> = repos.hide()

    fun fetchErrors(): Observable<Throwable> = fetchErrors.hide()

    fun networkErrors(): Observable<Throwable> = networkErrors.hide()

    fun loadingState(): Observable<Boolean> = loadingState.hide()

}