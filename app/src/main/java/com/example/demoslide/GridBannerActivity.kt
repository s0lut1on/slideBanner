package com.example.demoslide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class GridBannerActivity : AppCompatActivity() {
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

    private lateinit var snapHelper: androidx.recyclerview.widget.LinearSnapHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_banner)

        val gridBannerRecyclerView = findViewById<RecyclerView>(R.id.gridBannerRecyclerView)
        
        // Use GridLayoutManager for proper grid layout
        val gridLayoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3) // 3 columns
        gridBannerRecyclerView.layoutManager = gridLayoutManager
        
        val gridBannerAdapter = GridBannerAdapter(imageUrls)
        gridBannerRecyclerView.adapter = gridBannerAdapter

        // Snap to center for carousel effect
        snapHelper = androidx.recyclerview.widget.LinearSnapHelper()
        snapHelper.attachToRecyclerView(gridBannerRecyclerView)

        // Scroll to the middle for infinite effect
        val initialPosition = (gridBannerAdapter.itemCount / 2).let {
            it - (it % imageUrls.size)
        }
        gridBannerRecyclerView.scrollToPosition(initialPosition)

        // Prevent glitch by teleporting to the middle when reaching the start or end
        gridBannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val firstVisible = gridLayoutManager.findFirstVisibleItemPosition()
                    val lastVisible = gridLayoutManager.findLastVisibleItemPosition()
                    val itemCount = gridBannerAdapter.itemCount
                    val realCount = imageUrls.size

                    // If at the start, jump to the same item in the middle
                    if (firstVisible <= 1) {
                        val middle = (itemCount / 2) - ((itemCount / 2) % realCount)
                        recyclerView.scrollToPosition(middle + (firstVisible % realCount))
                    }
                    // If at the end, jump to the same item in the middle
                    else if (lastVisible >= itemCount - 2) {
                        val middle = (itemCount / 2) - ((itemCount / 2) % realCount)
                        recyclerView.scrollToPosition(middle + (lastVisible % realCount))
                    }
                }
            }
        })

        // Enhanced key listener to handle more key codes and stop scrolling
        gridBannerRecyclerView.setOnKeyListener { v, keyCode, event ->
            when (event.action) {
                android.view.KeyEvent.ACTION_UP -> {
                    // Stop scroll on any directional key release
                    when (keyCode) {
                        android.view.KeyEvent.KEYCODE_DPAD_LEFT,
                        android.view.KeyEvent.KEYCODE_DPAD_RIGHT,
                        android.view.KeyEvent.KEYCODE_DPAD_UP,
                        android.view.KeyEvent.KEYCODE_DPAD_DOWN,
                        android.view.KeyEvent.KEYCODE_DPAD_CENTER,
                        android.view.KeyEvent.KEYCODE_ENTER,
                        android.view.KeyEvent.KEYCODE_BACK -> {
                            // Completely stop all movement and cancel animations
                            gridBannerRecyclerView.stopScroll()
                            gridBannerRecyclerView.clearOnScrollListeners()
                            gridBannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                    super.onScrollStateChanged(recyclerView, newState)
                                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                        val firstVisible = gridLayoutManager.findFirstVisibleItemPosition()
                                        val lastVisible = gridLayoutManager.findLastVisibleItemPosition()
                                        val itemCount = gridBannerAdapter.itemCount
                                        val realCount = imageUrls.size

                                        // If at the start, jump to the same item in the middle
                                        if (firstVisible <= 1) {
                                            val middle = (itemCount / 2) - ((itemCount / 2) % realCount)
                                            recyclerView.scrollToPosition(middle + (firstVisible % realCount))
                                        }
                                        // If at the end, jump to the same item in the middle
                                        else if (lastVisible >= itemCount - 2) {
                                            val middle = (itemCount / 2) - ((itemCount / 2) % realCount)
                                            recyclerView.scrollToPosition(middle + (lastVisible % realCount))
                                        }
                                    }
                                }
                            })
                            snapHelper.attachToRecyclerView(null) // Disable snap helper
                            snapHelper.attachToRecyclerView(gridBannerRecyclerView) // Re-enable snap helper
                            
                            // Cancel any ongoing animations on child views
                            for (i in 0 until gridBannerRecyclerView.childCount) {
                                val child = gridBannerRecyclerView.getChildAt(i)
                                child?.clearAnimation()
                                child?.animate()?.cancel()
                            }
                            true
                        }
                        else -> false
                    }
                }
                else -> false
            }
        }
    }
} 