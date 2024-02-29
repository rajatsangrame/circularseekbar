package com.rajatsangrame.samplecircularseekbar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rajatsangrame.circularseekbar.CircularSeekbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seekbar: CircularSeekbar = findViewById(R.id.circularseekbar)
        seekbar.setThickness(24)
        seekbar.setBackgroundColor(Color.BLACK)
        seekbar.setProgressColor(Color.parseColor("#f5f5f5"))
        seekbar.setStartAngle(CircularSeekbar.StartAngle.TOP)
        seekbar.setThumbRadius(24)
        seekbar.setShowThumb(true)
        seekbar.setUpdateProgressOnTouch(true)
        seekbar.setProgress(20f)

        Log.d(TAG, "onCreate: ${seekbar.getProgress()}")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}