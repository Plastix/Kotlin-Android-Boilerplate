package io.github.plastix.kotlinboilerplate.ui.base.rx

import io.github.plastix.kotlinboilerplate.ui.base.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RxPresenterTest {

    @Mock
    lateinit var view: MvpView
    lateinit var sub: TestObserver<Boolean>
    lateinit var presenter: PresenterSubclass

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        sub = TestObserver.create()
        presenter = PresenterSubclass()
    }

    @Test
    fun noViewAttachedByDefault() {
        presenter.getViewState().subscribe(sub)

        sub.assertNoErrors()
        sub.assertNotComplete()
        sub.assertValue(false)
    }

    @Test
    fun bindViewUpdatesViewState() {
        presenter.getViewState().subscribe(sub)

        presenter.bindView(view)

        sub.assertNoErrors()
        sub.assertNotComplete()
        sub.assertValues(false, true)
    }

    @Test
    fun unbindViewUpdatesViewState() {
        presenter.getViewState().subscribe(sub)

        presenter.unbindView()

        sub.assertNoErrors()
        sub.assertNotComplete()
        sub.assertValues(false, false)
    }

    @Test
    fun addSubscriptionUpdatesCompositeSubscription() {
        Assert.assertTrue(presenter.getSubcriptions().size() == 0)
        presenter.addDisposable(Disposables.empty())
        Assert.assertTrue(presenter.getSubcriptions().size() == 1)
    }

    @Test
    fun onDestroyClearsSubscriptionsAndUpdatesView() {
        presenter.getViewState().subscribe(sub)

        presenter.addDisposable(Disposables.empty())

        Assert.assertTrue(presenter.getSubcriptions().size() == 1)

        presenter.onDestroy()

        sub.assertNoErrors()
        sub.assertValues(false)
        sub.assertComplete()

        Assert.assertTrue(presenter.getSubcriptions().size() == 0)

    }

    class PresenterSubclass : RxPresenter<MvpView>() {

        fun getSubcriptions(): CompositeDisposable = disposables
    }
}