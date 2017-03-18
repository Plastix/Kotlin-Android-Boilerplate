package io.github.plastix.kotlinboilerplate.ui.detail

import activitystarter.Arg
import android.databinding.DataBindingUtil
import android.os.Bundle
import io.github.plastix.kotlinboilerplate.ApplicationComponent
import io.github.plastix.kotlinboilerplate.R
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.databinding.ActivityDetailBinding
import io.github.plastix.kotlinboilerplate.extensions.enableToolbarBackButton
import io.github.plastix.kotlinboilerplate.ui.base.ViewModelActivity

open class DetailActivity : ViewModelActivity<DetailViewModel, ActivityDetailBinding>() {

    @Arg lateinit var repo: Repo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.detailToolbar)
        enableToolbarBackButton()
    }

    override fun onBind() {
        super.onBind()
        binding.viewModel = viewModel
    }

    override fun getViewBinding(): ActivityDetailBinding
            = DataBindingUtil.setContentView(this, R.layout.activity_detail)

    override fun injectDependencies(graph: ApplicationComponent) {
        graph.plus(DetailModule(this, repo))
                .injectTo(this)
    }
}
