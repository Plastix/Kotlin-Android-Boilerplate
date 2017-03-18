package io.github.plastix.kotlinboilerplate.extensions

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun Context.inflateLayout(
        layoutResId: Int,
        parent: ViewGroup? = null,
        attachToRoot: Boolean = parent != null
): View =
        LayoutInflater.from(this).inflate(layoutResId, parent, attachToRoot)

fun ImageView.loadImage(url: String) {
    Picasso.with(context).load(url).into(this)
}

fun View.showSnackbar(
        message: String,
        length: Int = Snackbar.LENGTH_LONG,
        f: (Snackbar.() -> Unit) = {}
) {
    Snackbar.make(this, message, length).apply {
        f()
        show()
    }
}

fun View.showSnackbar(
        @StringRes message: Int,
        length: Int = Snackbar.LENGTH_LONG,
        f: (Snackbar.() -> Unit) = {}
) {
    showSnackbar(resources.getString(message), length, f)
}

fun Snackbar.action(
        action: String,
        @ColorInt color: Int? = null,
        listener: (View) -> Unit
) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

val RecyclerView.childList: List<View>
    get() = (0..childCount - 1).map { getChildAt(it) }

val View.recyclerParams: RecyclerView.LayoutParams
    get() = layoutParams as RecyclerView.LayoutParams
