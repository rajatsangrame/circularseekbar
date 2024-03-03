package com.rajatsangrame.samplecircularseekbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.rajatsangrame.circularseekbar.BaseSeekbar;
import com.rajatsangrame.circularseekbar.CircularSeekbar;
import com.rajatsangrame.circularseekbar.OnProgressChangeListener;
import com.rajatsangrame.circularseekbar.RainbowSeekbar;
import com.rajatsangrame.circularseekbar.StartAngle;

import java.util.function.BiConsumer;

public class JavaSampleActivity extends AppCompatActivity {

    private static final String TAG = "JavaSample";
    private BaseSeekbar seekbar;
    private SeekBar thickness;
    private SeekBar thumbRadius;
    private SeekBar sweepAngle;

    private Spinner startAngle;
    private Spinner progressColor;
    private Spinner bgColor;
    private Spinner thumbColor;
    private Spinner selectseekbar;

    private SwitchMaterial enableTouch;
    private SwitchMaterial showThumb;

    private boolean firstRun = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(TAG);

        thickness = findViewById(R.id.thickness);
        thumbRadius = findViewById(R.id.thumbradius);
        sweepAngle = findViewById(R.id.sweepangle);
        final SeekBar.OnSeekBarChangeListener progressListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == R.id.thickness) {
                    int value = (progress * 40) / 100;
                    seekbar.setThickness(value);
                } else if (seekBar.getId() == R.id.thumbradius) {
                    int radius = (progress * 20) / 100;
                    seekbar.setThumbRadius(radius);
                } else if (seekBar.getId() == R.id.sweepangle) {
                    int value = progress * 360 / 100;
                    if (seekbar instanceof RainbowSeekbar) {
                        ((RainbowSeekbar) seekbar).setSweepAngle(value);
                    } else {
                        Toast.makeText(JavaSampleActivity.this,
                                "Swipe Angle not Supported",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
                seekbar.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        thickness.setOnSeekBarChangeListener(progressListener);
        thumbRadius.setOnSeekBarChangeListener(progressListener);
        sweepAngle.setOnSeekBarChangeListener(progressListener);

        startAngle = findViewById(R.id.startangle);
        progressColor = findViewById(R.id.progresscolor);
        bgColor = findViewById(R.id.bgcolor);
        thumbColor = findViewById(R.id.thumbcolor);
        selectseekbar = findViewById(R.id.selectseekbar);

        final AdapterView.OnItemSelectedListener callback = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.selectseekbar) {
                    if (!firstRun) {
                        getIntent().putExtra("seekbar", position);
                        finish();
                        startActivity(getIntent());
                    }
                    firstRun = false;
                }
                if (parent.getId() == R.id.startangle) {
                    if (seekbar instanceof CircularSeekbar) {
                        StartAngle angle = KotlinSampleActivity.getStartAngles().get(position);
                        ((CircularSeekbar) seekbar).setStartAngle(angle);
                    } else {
                        Toast.makeText(
                                JavaSampleActivity.this,
                                "Start Angle is not Supported",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else if (parent.getId() == R.id.bgcolor) {

                    if (position == 10) {
                        updateGradient(seekbar::setBackgroundGradient);
                    } else if (position == 11) {
                        seekbar.removeBackgroundGradient();
                    } else {
                        String color = KotlinSampleActivity.getMaterialColors().get(position);
                        seekbar.setBackgroundColor(Color.parseColor(color));
                    }

                } else if (parent.getId() == R.id.progresscolor) {

                    if (position == 10) {
                        updateGradient(seekbar::setProgressGradient);
                    } else if (position == 11) {
                        seekbar.removeProgressGradient();
                    } else {
                        String color = KotlinSampleActivity.getMaterialColors().get(position);
                        seekbar.setProgressColor(Color.parseColor(color));
                    }

                } else if (parent.getId() == R.id.thumbcolor) {
                    if (position == 10) {
                        updateGradient(seekbar::setThumbGradient);
                    } else if (position == 11) {
                        seekbar.removeThumbGradient();
                    } else {
                        String color = KotlinSampleActivity.getMaterialColors().get(position);
                        seekbar.setThumbColor(Color.parseColor(color));
                    }
                }
                seekbar.invalidate();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        startAngle.setOnItemSelectedListener(callback);
        progressColor.setOnItemSelectedListener(callback);
        bgColor.setOnItemSelectedListener(callback);
        thumbColor.setOnItemSelectedListener(callback);
        selectseekbar.setOnItemSelectedListener(callback);

        enableTouch = findViewById(R.id.enabletouch);
        showThumb = findViewById(R.id.showthumb);
        CompoundButton.OnCheckedChangeListener checkedChangeListener = (buttonView, isChecked) -> {
            if (buttonView.getId() == R.id.enabletouch) {
                seekbar.setEnableTouch(isChecked);
            } else if (buttonView.getId() == R.id.showthumb) {
                seekbar.setShowThumb(isChecked);
            }
            seekbar.invalidate();
        };
        enableTouch.setOnCheckedChangeListener(checkedChangeListener);
        showThumb.setOnCheckedChangeListener(checkedChangeListener);


        // Comment this to override values defined in the xml
        loadSeekBar();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                seekbar.setAnimatedProgress(75f, 600L);
            }
        }, 800);

        findViewById(R.id.button).setVisibility(View.GONE);

    }

    private void loadSeekBar() {
        int pos = getIntent().getIntExtra("seekbar", 0);
        if (pos == 0) {
            findViewById(R.id.circularseekbar).setVisibility(View.GONE);
            seekbar = findViewById(R.id.rainbowskeebar);
            seekbar.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.rainbowskeebar).setVisibility(View.GONE);
            seekbar = findViewById(R.id.circularseekbar);
            seekbar.setVisibility(View.VISIBLE);
        }
        seekbar.setProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onProgressChanged(@NonNull BaseSeekbar seekBar, float progress, boolean fromUser) {
                final TextView tv = findViewById(R.id.tvprogress);
                tv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTouchEvent(@NonNull BaseSeekbar seekBar) {

            }

            @Override
            public void onStopTouchEvent(@NonNull BaseSeekbar seekBar) {

            }
        });
        selectseekbar.setSelection(pos);

        bgColor.setSelection(8);
        progressColor.setSelection(10);
        thumbColor.setSelection(10);
        startAngle.setSelection(2);

        thickness.setProgress(100);
        thumbRadius.setProgress(100);
        sweepAngle.setProgress(100);

        showThumb.setChecked(true);
        enableTouch.setChecked(true);
    }

    private void updateGradient(BiConsumer<int[], float[]> fn) {
        int[] colors = {Color.RED, Color.YELLOW, Color.WHITE, Color.GREEN};
        float[] positions = {0f, 0.333f, 0.667f, 1f};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fn.accept(colors, positions);
        }
    }

}
