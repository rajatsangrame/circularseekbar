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
import com.rajatsangrame.circularseekbar.CircularSeekbar
import com.rajatsangrame.circularseekbar.Util.onProgressChanged

class KotlinSampleActivity : AppCompatActivity() {

    private lateinit var circularSeekbar: CircularSeekbar
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

        circularSeekbar = findViewById(R.id.circularseekbar)
        circularSeekbar.onProgressChanged { progress, _ ->
            findViewById<TextView>(R.id.tvprogress).text = "$progress"
        }

        thickness = findViewById(R.id.thickness)
        thumbRadius = findViewById(R.id.thumbradius)
        val progressListener = object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (seekBar) {
                    thickness -> {
                        val value = (progress * 40) / 100
                        circularSeekbar.setThickness(value)
                    }

                    thumbRadius -> {
                        val value = (progress * 20) / 100
                        circularSeekbar.setThumbRadius(value)
                    }
                }
                circularSeekbar.invalidate()
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


        val callback = { view: AdapterView<*>?, position: Int ->
            when {
                position == 0 -> Unit // Ignore

                view == startAngle -> {
                    val angle = startAngles[position]
                    circularSeekbar.setStartAngle(angle)
                    circularSeekbar.invalidate()
                }

                view == progressColor -> {
                    val color = materialColors[position]
                    circularSeekbar.setProgressColor(Color.parseColor(color))
                    circularSeekbar.invalidate()
                }

                view == bgColor -> {
                    val color = materialColors[position]
                    circularSeekbar.setBackgroundColor(Color.parseColor(color))
                    circularSeekbar.invalidate()
                }

                view == thumbColor -> {
                    val color = materialColors[position]
                    circularSeekbar.setThumbColor(Color.parseColor(color))
                    circularSeekbar.invalidate()
                }
            }
        }
        startAngle.onProgressChange(callback)
        progressColor.onProgressChange(callback)
        bgColor.onProgressChange(callback)
        thumbColor.onProgressChange(callback)


        enableTouch = findViewById(R.id.enabletouch)
        showThumb = findViewById(R.id.showthumb)
        val checkedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
                if (buttonView.id == R.id.enabletouch) {
                    circularSeekbar.setEnableTouch(isChecked)
                } else if (buttonView.id == R.id.showthumb) {
                    circularSeekbar.setShowThumb(isChecked)
                }
                circularSeekbar.invalidate()
            }
        enableTouch.setOnCheckedChangeListener(checkedChangeListener)
        showThumb.setOnCheckedChangeListener(checkedChangeListener)

        // Comment this to override values defined in the xml
        updateSeekbarValues()

        Handler(Looper.getMainLooper()).postDelayed({
            circularSeekbar.setAnimatedProgress(75f, 600L)
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

        showThumb.isChecked = true
        enableTouch.isChecked = true
    }

    companion object {
        private const val TAG = "KotlinSample"

        @JvmStatic
        val startAngles = listOf(
            CircularSeekbar.StartAngle.TOP, // Ignore 0 index
            CircularSeekbar.StartAngle.LEFT,
            CircularSeekbar.StartAngle.TOP,
            CircularSeekbar.StartAngle.RIGHT,
            CircularSeekbar.StartAngle.BOTTOM,
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

    private fun Spinner.onProgressChange(callback: (AdapterView<*>?, Int) -> Unit) {
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                callback(parent, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}