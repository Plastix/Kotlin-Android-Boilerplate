package io.github.plastix.kotlinboilerplate.ui.misc

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import io.github.plastix.kotlinboilerplate.ApplicationQualifier
import io.github.plastix.kotlinboilerplate.R
import io.github.plastix.kotlinboilerplate.extensions.childList
import io.github.plastix.kotlinboilerplate.extensions.recyclerParams
import javax.inject.Inject

/**
 * Simple divider decorator for a RecyclerView.
 *
 * Adapted from https://gist.github.com/polbins/e37206fbc444207c0e92
 */
class SimpleDividerItemDecoration @Inject constructor(
        @ApplicationQualifier context: Context
) : RecyclerView.ItemDecoration() {

    private val divider: Drawable = ContextCompat.getDrawable(context, R.drawable.line_divider)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        parent.childList.forEach { child ->
            val top = child.bottom + child.recyclerParams.bottomMargin
            val bottom = top + divider.intrinsicHeight

            drawDivider(c, left, top, right, bottom)
        }
    }

    private fun drawDivider(c: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
        divider.setBounds(left, top, right, bottom)
        divider.draw(c)
    }
}