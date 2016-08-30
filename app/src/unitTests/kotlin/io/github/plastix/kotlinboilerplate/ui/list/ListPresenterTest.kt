package io.github.plastix.kotlinboilerplate.ui.list

import io.github.plastix.kotlinboilerplate.data.network.NetworkInteractor
import io.github.plastix.kotlinboilerplate.data.remote.GithubApiService
import io.github.plastix.kotlinboilerplate.data.remote.model.Owner
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.data.remote.model.SearchResponse
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ListPresenterTest {

    @get:Rule @Suppress("unused")
    val schedulerRule: RxSchedulerRule = RxSchedulerRule()

    @Mock
    lateinit var apiService: GithubApiService

    @Mock
    lateinit var networkInteractor: NetworkInteractor

    @Mock
    lateinit var view: ListView

    lateinit var presenter: ListPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = ListPresenterImpl(apiService, networkInteractor)
        presenter.bindView(view)
    }

    @Test
    fun getKotlinRepos_shouldUpdateViewWithApiData() {
        Mockito.`when`(networkInteractor.hasNetworkConnectionCompletable())
                .thenReturn(Completable.complete())

        val repos: List<Repo> = listOf(mockRepo(), mockRepo())
        val response: SearchResponse = SearchResponse(0, repos)
        Mockito.`when`(apiService.repoSearch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(response))

        presenter.getKotlinRepos()

        Mockito.verify(apiService).repoSearch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())
        Mockito.verify(networkInteractor).hasNetworkConnectionCompletable()
        Mockito.verify(view, Mockito.times(1)).startLoading()
        Mockito.verify(view, Mockito.times(1)).updateList(repos)
    }

    @Test
    fun getKotlinRepos_shouldErrorWithNetworkMessage() {
        Mockito.`when`(networkInteractor.hasNetworkConnectionCompletable())
                .thenReturn(
                        Completable.error(NetworkInteractor.NetworkUnavailableException())
                )

        val response: SearchResponse = SearchResponse(0, listOf(mockRepo()))
        Mockito.`when`(apiService.repoSearch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(response))

        presenter.getKotlinRepos()

        Mockito.verify(networkInteractor).hasNetworkConnectionCompletable()
        Mockito.verify(view, Mockito.times(1)).startLoading()
        Mockito.verify(view, Mockito.times(1)).errorNoNetwork()
    }

    @Test
    fun getKotlinRepos_shouldErrorWithFetchMessage() {
        Mockito.`when`(networkInteractor.hasNetworkConnectionCompletable())
                .thenReturn(Completable.complete())

        Mockito.`when`(apiService.repoSearch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.error(Throwable("Error")))


        presenter.getKotlinRepos()

        Mockito.verify(apiService).repoSearch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())
        Mockito.verify(networkInteractor).hasNetworkConnectionCompletable()
        Mockito.verify(view, Mockito.times(1)).startLoading()
        Mockito.verify(view, Mockito.times(1)).errorFetchRepos()
    }

    fun mockRepo() = Repo("", "", Owner("", ""), "", 0, 0)

}