package com.example.demoslide

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridLayoutActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_layout)

        val gridRecyclerView = findViewById<RecyclerView>(R.id.gridRecyclerView)
        
        // Use GridLayoutManager for grid layout
        val layoutManager = GridLayoutManager(this, 3) // 3 columns
        gridRecyclerView.layoutManager = layoutManager
        
        val gridBannerAdapter = GridBannerAdapter(imageUrls)
        gridRecyclerView.adapter = gridBannerAdapter

        // Enhanced key listener for smooth scrolling
        gridRecyclerView.setOnKeyListener { v, keyCode, event ->
            when (event.action) {
                KeyEvent.ACTION_UP -> {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_LEFT,
                        KeyEvent.KEYCODE_DPAD_RIGHT,
                        KeyEvent.KEYCODE_DPAD_UP,
                        KeyEvent.KEYCODE_DPAD_DOWN,
                        KeyEvent.KEYCODE_DPAD_CENTER,
                        KeyEvent.KEYCODE_ENTER,
                        KeyEvent.KEYCODE_BACK -> {
                            // Stop any ongoing scroll
                            gridRecyclerView.stopScroll()
                            
                            // Cancel any ongoing animations on child views
                            for (i in 0 until gridRecyclerView.childCount) {
                                val child = gridRecyclerView.getChildAt(i)
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