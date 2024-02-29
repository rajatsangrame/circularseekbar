package com.rajatsangrame.samplecircularseekbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rajatsangrame.circularseekbar.CircularSeekbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seekbar: CircularSeekbar = findViewById(R.id.circularseekbar)
        seekbar.getProgress()
    }
}