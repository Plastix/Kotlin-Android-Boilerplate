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

class ListViewModelTest {

    @get:Rule @Suppress("unused")
    val schedulerRule = RxSchedulerRule()

    @Mock
    lateinit var apiService: GithubApiService

    @Mock
    lateinit var networkInteractor: NetworkInteractor

    lateinit var viewModel: ListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = ListViewModel(apiService, networkInteractor)
        viewModel.bind()
    }

    @Test
    fun getRepos_shouldReturnEmpty() {
        viewModel.getRepos().test().assertValue(emptyList())
    }

    @Test
    fun loadingState_shouldBeFalse() {
        viewModel.loadingState().test().assertValue(false)
    }

    @Test
    fun fetchErrors_shouldReturnNothing() {
        viewModel.fetchErrors().test().assertNoErrors()
    }

    @Test
    fun networkErrors_shouldReturnNothing() {
        viewModel.networkErrors().test().assertNoErrors()
    }

    @Test
    fun getKotlinRepos_shouldUpdateViewWithApiData() {
        Mockito.`when`(networkInteractor.hasNetworkConnectionCompletable())
                .thenReturn(Completable.complete())

        val repos: List<Repo> = listOf(mockRepo(), mockRepo())
        val response: SearchResponse = SearchResponse(0, repos)
        Mockito.`when`(apiService.repoSearch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(response))

        viewModel.fetchRepos()

        viewModel.getRepos().test().assertValue(repos)
        viewModel.loadingState().test().assertValue(false)
    }

    @Test
    fun getKotlinRepos_shouldErrorWithNetworkMessage() {
        val error = NetworkInteractor.NetworkUnavailableException()
        Mockito.`when`(networkInteractor.hasNetworkConnectionCompletable())
                .thenReturn(
                        Completable.error(error)
                )

        val response: SearchResponse = SearchResponse(0, listOf(mockRepo()))
        Mockito.`when`(apiService.repoSearch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(response))
        val networkObserver = viewModel.networkErrors().test()

        viewModel.fetchRepos()

        viewModel.loadingState().test().assertValue(false)
        networkObserver.assertValue(error)
    }

    @Test
    fun getKotlinRepos_shouldErrorWithFetchMessage() {
        Mockito.`when`(networkInteractor.hasNetworkConnectionCompletable())
                .thenReturn(Completable.complete())

        val error = Throwable("Error")
        Mockito.`when`(apiService.repoSearch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.error(error))
        val fetchObserver = viewModel.fetchErrors().test()

        viewModel.fetchRepos()

        viewModel.loadingState().test().assertValue(false)
        fetchObserver.assertValue(error)
    }

    fun mockRepo() = Repo("", "", Owner("", ""), "", 0, 0)

}