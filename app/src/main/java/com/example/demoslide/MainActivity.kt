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

        // Button click listener to switch to grid banner
        btnSwitchToGrid.setOnClickListener {
            val intent = Intent(this, GridBannerActivity::class.java)
            startActivity(intent)
        }
    }
}