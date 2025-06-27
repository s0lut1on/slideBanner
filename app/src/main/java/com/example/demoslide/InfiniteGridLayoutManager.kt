package com.example.demoslide

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfiniteGridLayoutManager(
    context: Context,
    private val spanCount: Int
) : LinearLayoutManager(context, VERTICAL, false) {

    companion object {
        private const val LOOP_MULTIPLIER = 1000
    }

    override fun canScrollHorizontally(): Boolean = true
    override fun canScrollVertically(): Boolean = true

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
        
        // Handle infinite horizontal scrolling
        if (dx > 0) { // Scrolling right
            val firstVisible = findFirstVisibleItemPosition()
            if (firstVisible <= spanCount) {
                val middle = (itemCount / 2) - ((itemCount / 2) % spanCount)
                scrollToPosition(middle + (firstVisible % spanCount))
            }
        } else if (dx < 0) { // Scrolling left
            val lastVisible = findLastVisibleItemPosition()
            if (lastVisible >= itemCount - spanCount) {
                val middle = (itemCount / 2) - ((itemCount / 2) % spanCount)
                scrollToPosition(middle + (lastVisible % spanCount))
            }
        }
        
        return scrolled
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scrolled = super.scrollVerticallyBy(dy, recycler, state)
        
        // Handle infinite vertical scrolling
        if (dy > 0) { // Scrolling down
            val firstVisible = findFirstVisibleItemPosition()
            if (firstVisible <= spanCount) {
                val middle = (itemCount / 2) - ((itemCount / 2) % spanCount)
                scrollToPosition(middle + (firstVisible % spanCount))
            }
        } else if (dy < 0) { // Scrolling up
            val lastVisible = findLastVisibleItemPosition()
            if (lastVisible >= itemCount - spanCount) {
                val middle = (itemCount / 2) - ((itemCount / 2) % spanCount)
                scrollToPosition(middle + (lastVisible % spanCount))
            }
        }
        
        return scrolled
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        
        // Start in the middle for infinite effect
        if (state?.isPreLayout == false && childCount == 0) {
            val middle = (itemCount / 2) - ((itemCount / 2) % spanCount)
            scrollToPosition(middle)
        }
    }
} 