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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.rajatsangrame.circularseekbar.BaseSeekbar
import com.rajatsangrame.circularseekbar.CircularSeekbar
import com.rajatsangrame.circularseekbar.RainbowSeekbar
import com.rajatsangrame.circularseekbar.StartAngle
import com.rajatsangrame.circularseekbar.Util.onProgressChanged

class KotlinSampleActivity : AppCompatActivity() {

    private lateinit var seekbar: BaseSeekbar
    private lateinit var thickness: SeekBar
    private lateinit var thumbRadius: SeekBar
    private lateinit var sweepAngle: SeekBar

    private lateinit var startAngle: Spinner
    private lateinit var progressColor: Spinner
    private lateinit var bgColor: Spinner
    private lateinit var thumbColor: Spinner
    private lateinit var selectseekbar: Spinner

    private lateinit var enableTouch: SwitchMaterial
    private lateinit var showThumb: SwitchMaterial

    private var firstRun = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = TAG

        thickness = findViewById(R.id.thickness)
        thumbRadius = findViewById(R.id.thumbradius)
        sweepAngle = findViewById(R.id.sweepangle)
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

                    R.id.sweepangle -> {
                        val value = progress * 360 / 100
                        if (seekbar is RainbowSeekbar) {
                            (seekbar as RainbowSeekbar).setSweepAngle(value.toFloat())
                        } else {
                            Toast.makeText(
                                this@KotlinSampleActivity,
                                "Swipe Angle not Supported",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                seekbar.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
        thickness.setOnSeekBarChangeListener(progressListener)
        thumbRadius.setOnSeekBarChangeListener(progressListener)
        sweepAngle.setOnSeekBarChangeListener(progressListener)

        startAngle = findViewById(R.id.startangle)
        progressColor = findViewById(R.id.progresscolor)
        bgColor = findViewById(R.id.bgcolor)
        thumbColor = findViewById(R.id.thumbcolor)
        selectseekbar = findViewById(R.id.selectseekbar)

        val callback: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    when (parent.id) {
                        R.id.selectseekbar -> {
                            if (!firstRun) {
                                intent.putExtra("seekbar", position)
                                finish()
                                startActivity(intent)
                            }
                            firstRun = false
                        }

                        R.id.startangle -> {
                            val angle = startAngles[position]
                            if (seekbar is CircularSeekbar) {
                                (seekbar as CircularSeekbar).setStartAngle(angle)
                            } else {
                                Toast.makeText(
                                    this@KotlinSampleActivity,
                                    "Start Angle is not Supported",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                        R.id.bgcolor -> {
                            if (position == 10) {
                                updateGradient(seekbar::setBackgroundGradient)
                            } else if (position == 11) {
                                seekbar.removeBackgroundGradient()
                            } else {
                                val color = materialColors[position]
                                seekbar.setBackgroundColor(Color.parseColor(color))
                            }
                        }

                        R.id.progresscolor -> {
                            if (position == 10) {
                                updateGradient(seekbar::setProgressGradient)
                            } else if (position == 11) {
                                seekbar.removeProgressGradient()
                            } else {
                                val color = materialColors[position]
                                seekbar.setProgressColor(Color.parseColor(color))
                            }
                        }

                        R.id.thumbcolor -> {

                            if (position == 10) {
                                updateGradient(seekbar::setThumbGradient)
                            } else if (position == 11) {
                                seekbar.removeThumbGradient()
                            } else {
                                val color = materialColors[position]
                                seekbar.setThumbColor(Color.parseColor(color))
                            }
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
        selectseekbar.onItemSelectedListener = callback

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
        loadSeekBar()

        Handler(Looper.getMainLooper()).postDelayed({
            seekbar.setAnimatedProgress(70f, 600L)
        }, 800)

        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, JavaSampleActivity::class.java))
        }
    }

    private fun loadSeekBar() {
        val pos = intent.getIntExtra("seekbar", 0)
        seekbar = if (pos == 0) {
            findViewById<CircularSeekbar>(R.id.circularseekbar).visibility = View.GONE
            val view = findViewById<RainbowSeekbar>(R.id.rainbowskeebar)
            view.visibility = View.VISIBLE
            view
        } else {
            findViewById<RainbowSeekbar>(R.id.rainbowskeebar).visibility = View.GONE
            val view = findViewById<CircularSeekbar>(R.id.circularseekbar)
            view.visibility = View.VISIBLE
            view
        }
        seekbar.onProgressChanged { progress, _ ->
            findViewById<TextView>(R.id.tvprogress).text = "$progress"
        }
        selectseekbar.setSelection(pos)

        bgColor.setSelection(8)
        progressColor.setSelection(10)
        thumbColor.setSelection(10)
        startAngle.setSelection(2)

        thickness.progress = 100
        thumbRadius.progress = 100
        sweepAngle.progress = 75

        showThumb.isChecked = true
        enableTouch.isChecked = true
    }

    private fun updateGradient(fn: (IntArray, FloatArray) -> Unit) {
        fn(
            intArrayOf(Color.RED, Color.YELLOW, Color.WHITE, Color.GREEN),
            floatArrayOf(0f, 0.333f, 0.667f, 1f)
        )
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
            "#05c46b",
            "Gradient",
            "RemoveGradient",
        )
    }
}