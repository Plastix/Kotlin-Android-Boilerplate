package io.github.plastix.kotlinboilerplate.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.StyleSpan
import android.widget.TextView
import io.github.plastix.kotlinboilerplate.ApplicationComponent
import io.github.plastix.kotlinboilerplate.R
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo
import io.github.plastix.kotlinboilerplate.extensions.enableToolbarBackButton
import io.github.plastix.kotlinboilerplate.extensions.loadImage
import io.github.plastix.kotlinboilerplate.ui.base.PresenterActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : PresenterActivity<DetailView, DetailPresenter>(), DetailView {

    companion object {
        val EXTRA_REPO_OBJECT = "REPO_ITEM"

        fun newIntent(context: Context, repo: Repo): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_REPO_OBJECT, repo)
            return intent
        }
    }

    private lateinit var repo: Repo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(detailToolbar)

        repo = intent.getParcelableExtra(EXTRA_REPO_OBJECT)

        enableToolbarBackButton()
        updateUI()
    }

    private fun updateUI() {
        detailOwnerAvatar.loadImage(repo.owner.avatarUrl)
        detailDescription.text = repo.description
        detailStarCount.text = repo.stars.toString()
        detailForkCount.text = repo.forks.toString()
        setFancyRepoName()
    }

    private fun setFancyRepoName() {
        val repoName = repo.fullName
        detailName.setText(repoName, TextView.BufferType.SPANNABLE)
        val dividerIndex = repoName.indexOf('/')
        val span = detailName.text as Spannable
        span.setSpan(StyleSpan(Typeface.BOLD), dividerIndex, repoName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    override fun injectDependencies(graph: ApplicationComponent) {
        graph.plus(DetailModule(this))
                .injectTo(this)
    }
}
