package io.github.plastix.kotlinboilerplate.data.network

import android.net.ConnectivityManager
import android.net.NetworkInfo
import io.reactivex.Completable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

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
        Mockito.`when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
    }

    @Test
    fun hasNetworkConnection_shouldReturnFalseWhenNoNetwork() {
        Mockito.`when`(connectivityManager.activeNetworkInfo).thenReturn(null)

        Assert.assertFalse(networkInteractor.hasNetworkConnection())
    }

    @Test
    fun `hasNetworkConnection_shouldReturnFalseWhenNotConnected`() {
        Mockito.`when`(networkInfo.isConnectedOrConnecting).thenReturn(false)

        Assert.assertFalse(networkInteractor.hasNetworkConnection())
    }

    @Test
    fun hasNetworkConnection_shouldReturnTrueWhenConnected() {
        Mockito.`when`(networkInfo.isConnectedOrConnecting).thenReturn(true)

        Assert.assertTrue(networkInteractor.hasNetworkConnection())
    }

    @Test
    fun hasNetworkConnectionCompletable_shouldCompleteWhenConnected() {
        Mockito.`when`(networkInfo.isConnectedOrConnecting).thenReturn(true)

        Assert.assertEquals(networkInteractor.hasNetworkConnectionCompletable(), Completable.complete())
    }

    @Test
    fun hasNetworkConnectionCompletable_shouldErrorWhenNotConnected() {
        Mockito.`when`(networkInfo.isConnectedOrConnecting).thenReturn(false)

        Assert.assertTrue(networkInteractor.hasNetworkConnectionCompletable().blockingGet() is NetworkInteractor.NetworkUnavailableException)
    }
}
