package com.example.demoslide

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSwitchToGrid = findViewById<Button>(R.id.btnSwitchToGrid)
        val btnSwitchToHorizontal = findViewById<Button>(R.id.btnSwitchToHorizontal)

        // Button click listener to switch to grid banner
        btnSwitchToGrid.setOnClickListener {
            val intent = Intent(this, GridBannerActivity::class.java)
            startActivity(intent)
        }

        // Button click listener to switch to horizontal scroll
        btnSwitchToHorizontal.setOnClickListener {
            val intent = Intent(this, HorizontalScrollActivity::class.java)
            startActivity(intent)
        }
    }
}