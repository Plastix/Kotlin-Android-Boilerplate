package io.github.plastix.kotlinboilerplate.ui.list

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.plastix.kotlinboilerplate.data.network.NetworkInteractor
import io.github.plastix.kotlinboilerplate.data.remote.GithubApiService
import io.github.plastix.kotlinboilerplate.data.remote.model.Owner
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.data.remote.model.SearchResponse
import io.github.plastix.kotlinboilerplate.util.RxSchedulersOverrideRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import rx.Completable
import rx.Single

class ListPresenterTest {

    @get:Rule @Suppress("unused")
    val schedulerRule: RxSchedulersOverrideRule = RxSchedulersOverrideRule()

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
        whenever(networkInteractor.hasNetworkConnectionCompletable())
                .thenReturn(Completable.complete())

        val repos: List<Repo> = listOf(mockRepo(), mockRepo())
        val response: SearchResponse = SearchResponse(0, repos)
        whenever(apiService.repoSearch(any(), any(), any()))
                .thenReturn(Single.just(response))

        presenter.getKotlinRepos()

        verify(apiService).repoSearch(any(), any(), any())
        verify(networkInteractor).hasNetworkConnectionCompletable()
        verify(view, times(1)).startLoading()
        verify(view, times(1)).updateList(repos)
    }

    @Test
    fun getKotlinRepos_shouldErrorWithNetworkMessage() {
        whenever(networkInteractor.hasNetworkConnectionCompletable())
                .thenReturn(
                        Completable.error(NetworkInteractor.NetworkUnavailableException())
                )

        val response: SearchResponse = SearchResponse(0, listOf(mockRepo()))
        whenever(apiService.repoSearch(any(), any(), any()))
                .thenReturn(Single.just(response))

        presenter.getKotlinRepos()

        verify(networkInteractor).hasNetworkConnectionCompletable()
        verify(view, times(1)).startLoading()
        verify(view, times(1)).errorNoNetwork()
    }

    @Test
    fun getKotlinRepos_shoulErrorWithFetchMessage() {
        whenever(networkInteractor.hasNetworkConnectionCompletable())
                .thenReturn(Completable.complete())

        whenever(apiService.repoSearch(any(), any(), any()))
                .thenReturn(Single.error(Throwable("Error")))


        presenter.getKotlinRepos()

        verify(apiService).repoSearch(any(), any(), any())
        verify(networkInteractor).hasNetworkConnectionCompletable()
        verify(view, times(1)).startLoading()
        verify(view, times(1)).errorFetchRepos()
    }

    fun mockRepo() = Repo("", "", Owner("", ""), "", 0, 0)

}