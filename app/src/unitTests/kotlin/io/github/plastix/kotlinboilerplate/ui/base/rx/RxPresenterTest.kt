package io.github.plastix.kotlinboilerplate.ui.base.rx

import io.github.plastix.kotlinboilerplate.ui.base.MvpView
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import rx.observers.TestSubscriber
import rx.subscriptions.CompositeSubscription
import rx.subscriptions.Subscriptions

class RxPresenterTest {

    @Mock
    lateinit var view: MvpView
    lateinit var sub: TestSubscriber<Boolean>
    lateinit var presenter: PresenterSubclass

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        sub = TestSubscriber()
        presenter = PresenterSubclass()
    }

    @Test
    fun noViewAttachedByDefault() {
        presenter.getViewState().subscribe(sub)

        sub.assertNoErrors()
        sub.assertNotCompleted()
        sub.assertValue(false)
    }

    @Test
    fun bindViewUpdatesViewState() {
        presenter.getViewState().subscribe(sub)

        presenter.bindView(view)

        sub.assertNoErrors()
        sub.assertNotCompleted()
        sub.assertValues(false, true)
    }

    @Test
    fun unbindViewUpdatesViewState() {
        presenter.getViewState().subscribe(sub)

        presenter.unbindView()

        sub.assertNoErrors()
        sub.assertNotCompleted()
        sub.assertValues(false, false)
    }

    @Test
    fun addSubscriptionUpdatesCompositeSubscription() {
        Assert.assertFalse(presenter.getSubcriptions().hasSubscriptions())
        presenter.addSubscription(Subscriptions.create { /* No op */ })
        Assert.assertTrue(presenter.getSubcriptions().hasSubscriptions())
    }

    @Test
    fun onDestroyClearsSubscriptionsAndUpdatesView() {
        presenter.getViewState().subscribe(sub)

        presenter.addSubscription(Subscriptions.create { /* No op */ })

        Assert.assertTrue(presenter.getSubcriptions().hasSubscriptions())

        presenter.onDestroy()

        sub.assertNoErrors()
        sub.assertValues(false)
        sub.assertCompleted()

        Assert.assertFalse(presenter.getSubcriptions().hasSubscriptions())

    }

    class PresenterSubclass : RxPresenter<MvpView>() {

        fun getSubcriptions(): CompositeSubscription = subscriptions
    }
}