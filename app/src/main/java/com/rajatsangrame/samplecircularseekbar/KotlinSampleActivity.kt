package com.rajatsangrame.samplecircularseekbar

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.rajatsangrame.circularseekbar.BaseSeekbar
import com.rajatsangrame.circularseekbar.RainbowSeekbar
import com.rajatsangrame.circularseekbar.StartAngle
import com.rajatsangrame.circularseekbar.Util.onProgressChanged

class KotlinSampleActivity : AppCompatActivity() {

    private lateinit var seekbar: BaseSeekbar
    private lateinit var thickness: SeekBar
    private lateinit var thumbRadius: SeekBar

    private lateinit var startAngle: Spinner
    private lateinit var progressColor: Spinner
    private lateinit var bgColor: Spinner
    private lateinit var thumbColor: Spinner
    private lateinit var enableTouch: SwitchMaterial
    private lateinit var showThumb: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = TAG

        seekbar = findViewById(R.id.circularseekbar)
        seekbar.onProgressChanged { progress, _ ->
            findViewById<TextView>(R.id.tvprogress).text = "$progress"
        }

        thickness = findViewById(R.id.thickness)
        thumbRadius = findViewById(R.id.thumbradius)
        val progressListener = object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                when (seekBar.id) {
                    R.id.thickness -> {
                        val value = (progress * 40) / 100
                        seekbar.setThickness(value)
                    }

                    R.id.thumbradius -> {
                        val value = (progress * 20) / 100
                        seekbar.setThumbRadius(value)
                    }
                }
                seekbar.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
        thickness.setOnSeekBarChangeListener(progressListener)
        thumbRadius.setOnSeekBarChangeListener(progressListener)

        startAngle = findViewById(R.id.startangle)
        progressColor = findViewById(R.id.progresscolor)
        bgColor = findViewById(R.id.bgcolor)
        thumbColor = findViewById(R.id.thumbcolor)

        val callback: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    when (parent.id) {
                        R.id.startangle -> {
                            val angle = startAngles[position]
                            //circularSeekbar.setStartAngle(angle)
                        }

                        R.id.bgcolor -> {
                            val color = materialColors[position]
                            seekbar.setBackgroundColor(Color.parseColor(color))
                        }

                        R.id.progresscolor -> {
                            val color = materialColors[position]
                            seekbar.setProgressColor(Color.parseColor(color))
                        }

                        R.id.thumbcolor -> {
                            val color = materialColors[position]
                            seekbar.setThumbColor(Color.parseColor(color))
                        }
                    }
                    seekbar.invalidate()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        startAngle.onItemSelectedListener = callback
        progressColor.onItemSelectedListener = callback
        bgColor.onItemSelectedListener = callback
        thumbColor.onItemSelectedListener = callback

        enableTouch = findViewById(R.id.enabletouch)
        showThumb = findViewById(R.id.showthumb)
        val checkedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
                if (buttonView.id == R.id.enabletouch) {
                    seekbar.setEnableTouch(isChecked)
                } else if (buttonView.id == R.id.showthumb) {
                    seekbar.setShowThumb(isChecked)
                }
                seekbar.invalidate()
            }
        enableTouch.setOnCheckedChangeListener(checkedChangeListener)
        showThumb.setOnCheckedChangeListener(checkedChangeListener)

        // Comment this to override values defined in the xml
        updateSeekbarValues()

        Handler(Looper.getMainLooper()).postDelayed({
            seekbar.setAnimatedProgress(25f, 600L)
        }, 800)

        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, JavaSampleActivity::class.java))
        }
    }

    private fun updateSeekbarValues() {
        bgColor.setSelection(8)
        progressColor.setSelection(1)
        thumbColor.setSelection(1)
        startAngle.setSelection(2)

        thickness.progress = 100
        thumbRadius.progress = 100

        showThumb.isChecked = false
        enableTouch.isChecked = true
        (seekbar as? RainbowSeekbar)?.setSweepAngle(270f)
    }

    companion object {
        private const val TAG = "KotlinSample"

        @JvmStatic
        val startAngles = listOf(
            StartAngle.TOP, // Ignore 0 index
            StartAngle.LEFT,
            StartAngle.TOP,
            StartAngle.RIGHT,
            StartAngle.BOTTOM,
        )

        @JvmStatic
        val materialColors = listOf(
            "Select",
            "#FFC312",
            "#ED4C67",
            "#5758BB",
            "#ffa801",
            "#A3CB38",
            "#9980FA",
            "#EA2027",
            "#485460",
            "#05c46b"
        )
    }
}