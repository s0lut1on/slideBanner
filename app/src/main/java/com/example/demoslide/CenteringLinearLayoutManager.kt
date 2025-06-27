package com.example.demoslide

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearSmoothScroller

class CenteringLinearLayoutManager(
    context: Context,
    orientation: Int,
    reverseLayout: Boolean
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
            override fun getHorizontalSnapPreference(): Int = SNAP_TO_START
            override fun getVerticalSnapPreference(): Int = SNAP_TO_START

            override fun calculateDxToMakeVisible(view: android.view.View, snapPreference: Int): Int {
                if (orientation == HORIZONTAL) {
                    val parentCenter = width / 2
                    val childCenter = getDecoratedLeft(view) + (getDecoratedMeasuredWidth(view) / 2)
                    return parentCenter - childCenter
                }
                return 0
            }

            override fun calculateDyToMakeVisible(view: android.view.View, snapPreference: Int): Int {
                if (orientation == VERTICAL) {
                    val parentCenter = height / 2
                    val childCenter = getDecoratedTop(view) + (getDecoratedMeasuredHeight(view) / 2)
                    return parentCenter - childCenter
                }
                return 0
            }
        }
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    fun forceStopScroll() {
        // Force stop any ongoing scroll animations
        try {
            val recyclerView = findViewByPosition(0)?.parent as? RecyclerView
            recyclerView?.stopScroll()
            recyclerView?.clearOnScrollListeners()
        } catch (e: Exception) {
            // Ignore any exceptions
        }
    }
} 