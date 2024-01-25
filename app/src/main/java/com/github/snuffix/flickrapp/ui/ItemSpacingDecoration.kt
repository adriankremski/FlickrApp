package com.github.snuffix.flickrapp.ui

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecoration(context: Context, private val gridSpanCount: Int = 3) :
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
            val column = position % gridSpanCount
            val row = position / gridSpanCount

            // Add spacing to the right and bottom, except for the last column and last row
            outRect.right = spacing
            outRect.bottom = spacing

            if (column == gridSpanCount - 1) {
                outRect.right = 0
            }

            if (row == layoutManager.spanCount - 1) {
                outRect.bottom = 0
            }
        } else {
            outRect.bottom = spacing
        }
    }
}
