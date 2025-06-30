package com.example.demoslide

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class HorizontalScrollActivity : AppCompatActivity() {
    private val imageUrls = listOf(
        "https://www.gstatic.com/webp/gallery/1.webp",
        "https://www.gstatic.com/webp/gallery/2.webp",
        "https://www.gstatic.com/webp/gallery/3.webp",
        "https://www.gstatic.com/webp/gallery/4.webp",
        "https://www.gstatic.com/webp/gallery/5.webp",
        "https://www.gstatic.com/webp/gallery/1.webp",
        "https://www.gstatic.com/webp/gallery/2.webp",
        "https://www.gstatic.com/webp/gallery/3.webp",
        "https://www.gstatic.com/webp/gallery/4.webp",
        "https://www.gstatic.com/webp/gallery/5.webp"
    )

    private lateinit var snapHelper: LinearSnapHelper
    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var horizontalAdapter: HorizontalScrollAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_scroll)

        horizontalRecyclerView = findViewById(R.id.horizontalRecyclerView)
        
        // Use LinearLayoutManager for horizontal scrolling
        layoutManager = object : LinearLayoutManager(this, HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                // Force width to be a fraction of the screen width
                lp?.width = (width * 0.4).toInt() // 40% of screen width
                return true
            }
        }
        horizontalRecyclerView.layoutManager = layoutManager
        
        horizontalAdapter = HorizontalScrollAdapter(imageUrls)
        horizontalRecyclerView.adapter = horizontalAdapter

        // Add padding to show multiple items
        val screenWidth = resources.displayMetrics.widthPixels
        val itemWidth = (screenWidth * 0.4).toInt() // 40% of screen width
        val sidePadding = ((screenWidth - itemWidth) / 2).toInt()
        horizontalRecyclerView.setPadding(sidePadding, 0, sidePadding, 0)
        horizontalRecyclerView.clipToPadding = false

        // Use LinearSnapHelper for smooth snapping
        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(horizontalRecyclerView)

        // Scroll to the middle for infinite effect
        val initialPosition = (horizontalAdapter.itemCount / 2).let {
            it - (it % imageUrls.size)
        }
        horizontalRecyclerView.post {
            horizontalRecyclerView.scrollToPosition(initialPosition)
        }

        // Handle scroll state changes
        horizontalRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    handleScrollPosition()
                }
            }
        })

        // Handle key events
        horizontalRecyclerView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        val currentPosition = getCurrentPosition()
                        if (currentPosition > 0) {
                            horizontalRecyclerView.smoothScrollToPosition(currentPosition - 1)
                        }
                        true
                    }
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        val currentPosition = getCurrentPosition()
                        if (currentPosition < horizontalAdapter.itemCount - 1) {
                            horizontalRecyclerView.smoothScrollToPosition(currentPosition + 1)
                        }
                        true
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER,
                    KeyEvent.KEYCODE_ENTER -> {
                        handleItemClick()
                        true
                    }
                    else -> false
                }
            } else false
        }
    }

    private fun getCurrentPosition(): Int {
        val snapView = snapHelper.findSnapView(layoutManager)
        return snapView?.let { layoutManager.getPosition(it) } ?: layoutManager.findFirstVisibleItemPosition()
    }

    private fun handleScrollPosition() {
        val currentPosition = getCurrentPosition()
        val itemCount = horizontalAdapter.itemCount
        val realCount = imageUrls.size
        val middle = (itemCount / 2) - ((itemCount / 2) % realCount)

        when {
            // If near the start, jump to the middle section
            currentPosition < realCount -> {
                val targetPosition = middle + (currentPosition % realCount)
                horizontalRecyclerView.post {
                    horizontalRecyclerView.scrollToPosition(targetPosition)
                }
            }
            // If near the end, jump to the middle section
            currentPosition > itemCount - realCount -> {
                val targetPosition = middle + (currentPosition % realCount)
                horizontalRecyclerView.post {
                    horizontalRecyclerView.scrollToPosition(targetPosition)
                }
            }
        }
    }

    private fun handleItemClick() {
        val snapView = snapHelper.findSnapView(layoutManager)
        snapView?.requestFocus()
    }
} 