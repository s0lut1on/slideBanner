package com.example.demoslide

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearSnapHelper

class MainActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_main)

        val bannerRecyclerView = findViewById<RecyclerView>(R.id.bannerRecyclerView)
        val btnSwitchToVertical = findViewById<Button>(R.id.btnSwitchToVertical)
        val btnSwitchToGrid = findViewById<Button>(R.id.btnSwitchToGrid)
        
        bannerRecyclerView.layoutManager = CenteringLinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val bannerAdapter = BannerAdapter(imageUrls)
        bannerRecyclerView.adapter = bannerAdapter

        // Snap to center for carousel effect
        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(bannerRecyclerView)

        // Scroll to the middle for infinite effect
        val initialPosition = (bannerAdapter.itemCount / 2).let {
            it - (it % imageUrls.size)
        }
        bannerRecyclerView.scrollToPosition(initialPosition)

        // Button click listener to switch to vertical banner
        btnSwitchToVertical.setOnClickListener {
            val intent = Intent(this, VerticalBannerActivity::class.java)
            startActivity(intent)
        }

        // Button click listener to switch to grid banner
        btnSwitchToGrid.setOnClickListener {
            val intent = Intent(this, GridBannerActivity::class.java)
            startActivity(intent)
        }

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
                android.view.KeyEvent.ACTION_DOWN -> {
                    when (keyCode) {
                        android.view.KeyEvent.KEYCODE_DPAD_CENTER,
                        android.view.KeyEvent.KEYCODE_ENTER -> {
                            // Navigate to vertical banner screen
                            val intent = Intent(this, VerticalBannerActivity::class.java)
                            startActivity(intent)
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