package io.github.plastix.kotlinboilerplate.data.network

import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import rx.Completable

class NetworkInteractorTest {

    lateinit var networkInteractor: NetworkInteractor

    @Mock
    lateinit var connectivityManager: ConnectivityManager

    @Mock
    lateinit var networkInfo: NetworkInfo

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        networkInteractor = NetworkInteractorImpl(connectivityManager)
        whenever(connectivityManager.activeNetworkInfo).then { networkInfo }
    }

    @Test
    fun hasNetworkConnection_shouldReturnFalseWhenNoNetwork() {
        whenever(connectivityManager.activeNetworkInfo).then { null }

        Assert.assertFalse(networkInteractor.hasNetworkConnection())
    }

    @Test
    fun `hasNetworkConnection_shouldReturnFalseWhenNotConnected`() {
        whenever(networkInfo.isConnectedOrConnecting).then { false }

        Assert.assertFalse(networkInteractor.hasNetworkConnection())
    }

    @Test
    fun hasNetworkConnection_shouldReturnTrueWhenConnected() {
        whenever(networkInfo.isConnectedOrConnecting).then { true }

        Assert.assertTrue(networkInteractor.hasNetworkConnection())
    }

    @Test
    fun hasNetworkConnectionCompletable_shouldCompleteWhenConnected() {
        whenever(networkInfo.isConnectedOrConnecting).then { true }

        Assert.assertEquals(networkInteractor.hasNetworkConnectionCompletable(), Completable.complete())
    }

    @Test
    fun hasNetworkConnectionCompletable_shouldErrorWhenNotConnected() {
        whenever(networkInfo.isConnectedOrConnecting).then { false }

        Assert.assertTrue(networkInteractor.hasNetworkConnectionCompletable().get() is NetworkInteractor.NetworkUnavailableException)
    }
}
