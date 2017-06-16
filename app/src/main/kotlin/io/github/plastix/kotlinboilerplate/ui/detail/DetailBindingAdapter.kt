package io.github.plastix.kotlinboilerplate.ui.detail

import android.databinding.BindingAdapter
import android.widget.ImageView
import io.github.plastix.kotlinboilerplate.extensions.loadImage

@BindingAdapter("android:src")
fun setImageBinding(view: ImageView, url: String) {
    view.loadImage(url)
}