package io.github.plastix.kotlinboilerplate.ui.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import io.github.plastix.kotlinboilerplate.ApplicationComponent
import io.github.plastix.kotlinboilerplate.R
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.databinding.ActivityListBinding
import io.github.plastix.kotlinboilerplate.extensions.isVisible
import io.github.plastix.kotlinboilerplate.extensions.showSnackbar
import io.github.plastix.kotlinboilerplate.ui.base.ViewModelActivity
import io.github.plastix.kotlinboilerplate.ui.detail.DetailActivityStarter
import io.github.plastix.kotlinboilerplate.ui.misc.SimpleDividerItemDecoration
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class ListActivity : ViewModelActivity<ListViewModel, ActivityListBinding>() {

    @Inject
    lateinit var listAdapter: RepoAdapter

    @Inject
    lateinit var listLayoutManager: LinearLayoutManager

    @Inject
    lateinit var dividerDecorator: SimpleDividerItemDecoration

    val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.listToolbar)
    }

    override fun onBind() {
        super.onBind()
        binding.viewModel = viewModel
        setupRecyclerView()
        setupSwipeRefresh()
        updateEmptyView()
        addDisposables()
    }

    override fun getViewBinding(): ActivityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)

    private fun setupSwipeRefresh() {
        binding.listSwipeRefresh.setOnRefreshListener {
            viewModel.fetchRepos()
        }
    }

    private fun setupRecyclerView() {
        binding.listRecyclerView.apply {
            adapter = listAdapter
            layoutManager = listLayoutManager
            addItemDecoration(dividerDecorator)
        }
        listAdapter.setClickListener(this::onItemClick)
    }

    override fun injectDependencies(graph: ApplicationComponent) {
        graph.plus(ListModule(this))
                .injectTo(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun onItemClick(repo: Repo) {
        DetailActivityStarter.start(this, repo)
    }

    private fun updateList(repos: List<Repo>) {
        listAdapter.updateRepos(repos)
        updateEmptyView()
    }

    private fun updateEmptyView() {
        val thereIsNoItems = listAdapter.itemCount == 0
        binding.emptyView.root.isVisible = thereIsNoItems
    }

    private fun addDisposables() {
        disposables.apply {
            add(viewModel.getRepos().subscribe(this@ListActivity::updateList))
            add(viewModel.loadingState().subscribe(binding.listSwipeRefresh::setRefreshing))
            add(viewModel.fetchErrors().subscribe(this@ListActivity::errorFetchRepos))
            add(viewModel.networkErrors().subscribe(this@ListActivity::errorNoNetwork))
        }
    }

    private fun errorNoNetwork(throwable: Throwable) {
        binding.listCoordinatorLayout.showSnackbar(R.string.list_error_no_network)
    }

    private fun errorFetchRepos(throwable: Throwable) {
        binding.listCoordinatorLayout.showSnackbar(R.string.list_error_failed_fetch)
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> {
            Timber.d("Settings menu clicked!")
            true
        }
        R.id.action_night -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            recreate()
            true
        }
        R.id.action_day -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            recreate()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
