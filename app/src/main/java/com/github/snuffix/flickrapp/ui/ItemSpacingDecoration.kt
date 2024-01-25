package com.github.snuffix.flickrapp.ui

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecoration(context: Context, private val maxGridSpanCount: Int = 3) :
    RecyclerView.ItemDecoration() {

    private val spacing: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        8f,
        context.resources.displayMetrics
    ).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val layoutManager = parent.layoutManager

        if (layoutManager is GridLayoutManager) {
            val column = position % maxGridSpanCount
            val row = position / maxGridSpanCount

            // Add spacing to the right and bottom, except for the last column and last row
            outRect.right = if (column == maxGridSpanCount - 1) 0 else spacing
            outRect.bottom = if (row == layoutManager.spanCount - 1) 0 else spacing
        } else {
            outRect.bottom = spacing
        }
    }
}
