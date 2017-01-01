package io.github.plastix.kotlinboilerplate.ui.list

import io.github.plastix.kotlinboilerplate.data.remote.model.Owner
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RepoViewModelTest {


    lateinit var owner: Owner
    lateinit var repo: Repo
    lateinit var viewModel: RepoViewModel

    @Before
    fun setUp() {
        owner = Owner("Author",
                "someURL")

        repo = Repo("Name",
                "Author/Name",
                owner,
                "Some random repo",
                50,
                100)

        viewModel = RepoViewModel(repo)
        viewModel.bind()
    }

    @Test
    fun getName_returnsCorrectName() {
        Assert.assertEquals(viewModel.getName(), repo.fullName)
    }

    @Test
    fun getDescription_returnsCorrectDescription() {
        Assert.assertEquals(viewModel.getDescription(), repo.description)
    }

    @Test
    fun clicks_returnsNoClicks() {
        viewModel.clicks().test().assertNoValues()
    }

    @Test
    fun clicks_clicksOnce() {
        val observer = viewModel.clicks().test()

        viewModel.onClick()
        observer.assertValueCount(1)
    }
}