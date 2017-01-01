package io.github.plastix.kotlinboilerplate.ui.list

import android.support.v7.util.DiffUtil
import io.github.plastix.kotlinboilerplate.data.remote.model.Repo

class RepoDiffCallback(private val old: List<Repo>, private val new: List<Repo>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].fullName == new[newItemPosition].fullName
    }

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == new[newItemPosition]
    }
}