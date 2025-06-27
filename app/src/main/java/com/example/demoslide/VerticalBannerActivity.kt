package com.example.demoslide

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearSnapHelper

class VerticalBannerActivity : AppCompatActivity() {
    private val imageUrls = listOf(
        "https://www.gstatic.com/webp/gallery/1.webp",
        "https://www.gstatic.com/webp/gallery/2.webp",
        "https://www.gstatic.com/webp/gallery/3.webp",
        "https://www.gstatic.com/webp/gallery/4.webp",
        "https://www.gstatic.com/webp/gallery/5.webp"
    )

    private lateinit var snapHelper: LinearSnapHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_banner)

        val bannerRecyclerView = findViewById<RecyclerView>(R.id.verticalBannerRecyclerView)
        val btnSwitchToHorizontal = findViewById<Button>(R.id.btnSwitchToHorizontal)
        
        bannerRecyclerView.layoutManager = CenteringLinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val bannerAdapter = VerticalBannerAdapter(imageUrls)
        bannerRecyclerView.adapter = bannerAdapter

        // Button click listener to switch back to horizontal banner
        btnSwitchToHorizontal.setOnClickListener {
            finish() // Go back to previous activity
        }

        // Snap to center for carousel effect
        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(bannerRecyclerView)

        // Scroll to the middle for infinite effect
        val initialPosition = (bannerAdapter.itemCount / 2).let {
            it - (it % imageUrls.size)
        }
        bannerRecyclerView.scrollToPosition(initialPosition)

        // Prevent glitch by teleporting to the middle when reaching the start or end
        bannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as CenteringLinearLayoutManager
                    val firstVisible = layoutManager.findFirstVisibleItemPosition()
                    val lastVisible = layoutManager.findLastVisibleItemPosition()
                    val itemCount = bannerAdapter.itemCount
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
        bannerRecyclerView.setOnKeyListener { v, keyCode, event ->
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
                            bannerRecyclerView.stopScroll()
                            bannerRecyclerView.clearOnScrollListeners()
                            bannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                    super.onScrollStateChanged(recyclerView, newState)
                                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                        val layoutManager = recyclerView.layoutManager as CenteringLinearLayoutManager
                                        val firstVisible = layoutManager.findFirstVisibleItemPosition()
                                        val lastVisible = layoutManager.findLastVisibleItemPosition()
                                        val itemCount = bannerAdapter.itemCount
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
                            snapHelper.attachToRecyclerView(bannerRecyclerView) // Re-enable snap helper
                            (bannerRecyclerView.layoutManager as? CenteringLinearLayoutManager)?.forceStopScroll()
                            
                            // Cancel any ongoing animations on child views
                            for (i in 0 until bannerRecyclerView.childCount) {
                                val child = bannerRecyclerView.getChildAt(i)
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