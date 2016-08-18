package io.github.plastix.kotlinboilerplate.ui.base

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AbstractPresenterTest {

    lateinit var view: MvpView
    lateinit var presenter: PresenterSubclass

    @Before
    fun setUp() {
        view = mock()
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

        verifyZeroInteractions(view)
    }

    class PresenterSubclass : AbstractPresenter<MvpView>() {

        fun getViewInstance(): MvpView? = view

    }
}