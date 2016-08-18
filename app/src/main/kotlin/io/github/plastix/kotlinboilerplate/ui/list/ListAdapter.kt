package io.github.plastix.kotlinboilerplate.ui.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.github.plastix.kotlinboilerplate.R
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.extensions.inflateLayout
import kotlinx.android.synthetic.main.item_repo.view.*
import java.util.*
import javax.inject.Inject

class ListAdapter @Inject constructor() : RecyclerView.Adapter<ListAdapter.RepoViewHolder>() {

    private var repos: List<Repo>
    private var itemClick: ((Repo) -> Unit)?

    init {
        repos = emptyList()
        itemClick = null
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = repos[position]
        holder.bindRepo(repo)
    }

    override fun getItemCount(): Int = repos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = parent.context.inflateLayout(R.layout.item_repo, parent, false)
        return RepoViewHolder(view, itemClick)
    }

    fun updateRepos(repos: List<Repo>) {
        this.repos = repos
        notifyDataSetChanged()
    }

    fun setClickListener(itemClick: ((Repo) -> Unit)?) {
        this.itemClick = itemClick
    }

    fun getRepos() = ArrayList(repos)

    class RepoViewHolder(itemView: View,
                         val itemClick: ((Repo) -> Unit)?) : RecyclerView.ViewHolder(itemView) {

        fun bindRepo(repo: Repo) = with(itemView) {
            repo_name.text = repo.fullName
            repo_description.text = repo.description

            itemView.setOnClickListener {
                itemClick?.invoke(repo)
            }
        }

    }
}