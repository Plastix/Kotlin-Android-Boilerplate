package io.github.plastix.kotlinboilerplate.ui.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RxViewModelTest {

    lateinit var sub: TestObserver<Boolean>
    lateinit var viewModel: ViewModelSubclass

    @Before
    fun setup() {
        sub = TestObserver.create()
        viewModel = ViewModelSubclass()
    }

    @Test
    fun noViewAttachedByDefault() {
        viewModel.getViewState().subscribe(sub)

        sub.assertNoErrors()
        sub.assertNotComplete()
        sub.assertValue(false)
    }

    @Test
    fun bindViewUpdatesViewState() {
        viewModel.getViewState().subscribe(sub)

        viewModel.bind()

        sub.assertNoErrors()
        sub.assertNotComplete()
        sub.assertValues(false, true)
    }

    @Test
    fun unbindViewUpdatesViewState() {
        viewModel.getViewState().subscribe(sub)

        viewModel.unbind()

        sub.assertNoErrors()
        sub.assertNotComplete()
        sub.assertValues(false, false)
    }

    @Test
    fun addSubscriptionUpdatesCompositeSubscription() {
        Assert.assertTrue(viewModel.getSubcriptions().size() == 0)
        viewModel.addDisposable(Disposables.empty())
        Assert.assertTrue(viewModel.getSubcriptions().size() == 1)
    }

    @Test
    fun onDestroyClearsSubscriptionsAndUpdatesView() {
        viewModel.getViewState().subscribe(sub)

        viewModel.addDisposable(Disposables.empty())

        Assert.assertTrue(viewModel.getSubcriptions().size() == 1)

        viewModel.onDestroy()

        sub.assertNoErrors()
        sub.assertValues(false)
        sub.assertComplete()

        Assert.assertTrue(viewModel.getSubcriptions().size() == 0)

    }

    class ViewModelSubclass : RxViewModel() {

        fun getSubcriptions(): CompositeDisposable = disposables
    }
}