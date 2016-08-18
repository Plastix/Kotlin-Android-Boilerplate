package io.github.plastix.kotlinboilerplate.extensions

import android.support.v7.app.AppCompatActivity

/**
 * Remember to set the android:parentActivityName attribute on the activity you are calling this
 * from!
 */
fun AppCompatActivity.enableToolbarBackButton() {
    delegate.supportActionBar?.setDisplayHomeAsUpEnabled(true)
}
