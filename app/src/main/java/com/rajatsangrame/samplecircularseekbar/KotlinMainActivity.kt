package com.rajatsangrame.samplecircularseekbar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.rajatsangrame.circularseekbar.CircularSeekbar

class KotlinMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seekbar: CircularSeekbar = findViewById(R.id.circularseekbar)
        seekbar.setBackgroundColor(Color.BLACK)
        seekbar.setProgressColor(Color.parseColor("#a5a5a5"))
        seekbar.setThumbColor(Color.parseColor("#f5f5f5"))
        seekbar.setStartAngle(CircularSeekbar.StartAngle.TOP)
        seekbar.setThickness(24)
        seekbar.setThumbRadius(16)
        seekbar.setThumbPadding(8)
        seekbar.setShowThumb(true)
        seekbar.setUpdateProgressOnTouch(true)
        seekbar.setProgress(20f)

        Handler(Looper.getMainLooper()).postDelayed({
            seekbar.setAnimatedProgress(75f, 800L)
        }, 800)


        Log.d(TAG, "onCreate: ${seekbar.getProgress()}")
    }

    companion object {
        private const val TAG = "KotlinMainActivity"
    }
}