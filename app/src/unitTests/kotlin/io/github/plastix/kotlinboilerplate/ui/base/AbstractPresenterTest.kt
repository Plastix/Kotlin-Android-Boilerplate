package io.github.plastix.kotlinboilerplate.ui.base

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AbstractPresenterTest {

    @Mock
    lateinit var view: MvpView
    lateinit var presenter: PresenterSubclass

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = PresenterSubclass()
    }

    @Test
    fun bindView_shouldSetCorrectView() {
        presenter.bindView(view)

        Assert.assertEquals(presenter.getViewInstance(), view)
    }

    @Test
    fun unbindView_shouldRemoveCurrentView() {
        presenter.unbindView()

        Assert.assertEquals(presenter.getViewInstance(), null)
    }

    @Test
    fun onDestroy_shouldNotModifyView() {
        presenter.onDestroy()

        Mockito.verifyZeroInteractions(view)
    }

    class PresenterSubclass : AbstractPresenter<MvpView>() {

        fun getViewInstance(): MvpView? = view

    }
}