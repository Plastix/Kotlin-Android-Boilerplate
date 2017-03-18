package io.github.plastix.kotlinboilerplate.ui.list

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.plastix.kotlinboilerplate.R
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.databinding.ItemRepoBinding
import io.github.plastix.kotlinboilerplate.ui.ActivityScope
import javax.inject.Inject

@ActivityScope
class RepoAdapter @Inject constructor() : RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    private var repos: List<Repo> = emptyList()
    private var itemClick: ((Repo) -> Unit)? = null

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val binding = holder.binding
        val repo = repos[position]
        var viewModel = binding.viewModel

        // Unbind old  iewModel if we have one
        viewModel?.unbind()

        // Create new ViewModel, set it, and bind it
        viewModel = RepoViewModel(repo)
        binding.viewModel = viewModel
        viewModel.bind()

        holder.setClickListener(itemClick)
    }

    override fun getItemCount(): Int = repos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = DataBindingUtil.inflate<ItemRepoBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_repo,
                parent,
                false
        )

        return RepoViewHolder(binding)
    }

    fun updateRepos(repos: List<Repo>) {
        val diff = RepoDiffCallback(this.repos, repos)
        val result = DiffUtil.calculateDiff(diff)

        this.repos = repos
        result.dispatchUpdatesTo(this)
    }

    fun setClickListener(itemClick: ((Repo) -> Unit)?) {
        this.itemClick = itemClick
    }

    class RepoViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setClickListener(callback: ((Repo) -> Unit)?) {
            binding.viewModel.clicks().subscribe {
                callback?.invoke(binding.viewModel.repo)
            }
        }

    }
}