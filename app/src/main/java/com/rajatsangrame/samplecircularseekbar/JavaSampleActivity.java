package com.rajatsangrame.samplecircularseekbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.rajatsangrame.circularseekbar.CircularSeekbar;

public class JavaSampleActivity extends AppCompatActivity {

    private static final String TAG = "JavaSample";
    private CircularSeekbar circularSeekbar;
    private SeekBar thickness;
    private SeekBar thumbRadius;
    private Spinner startAngle;
    private Spinner progressColor;
    private Spinner bgColor;
    private Spinner thumbColor;
    private SwitchMaterial enableTouch;
    private SwitchMaterial showThumb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(TAG);

        circularSeekbar = findViewById(R.id.circularseekbar);
        circularSeekbar.setProgressChangeListener(new CircularSeekbar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(@NonNull CircularSeekbar seekBar, float progress, boolean fromUser) {
                final TextView tv = findViewById(R.id.tvprogress);
                tv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTouchEvent(@NonNull CircularSeekbar seekBar) {

            }

            @Override
            public void onStopTouchEvent(@NonNull CircularSeekbar seekBar) {

            }
        });

        thickness = findViewById(R.id.thickness);
        thumbRadius = findViewById(R.id.thumbradius);
        final SeekBar.OnSeekBarChangeListener progressListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == R.id.thickness) {
                    int value = (progress * 40) / 100;
                    circularSeekbar.setThickness(value);
                } else if (seekBar.getId() == R.id.thumbradius) {
                    int radius = (progress * 20) / 100;
                    circularSeekbar.setThumbRadius(radius);
                }
                circularSeekbar.invalidate();
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

        startAngle = findViewById(R.id.startangle);
        progressColor = findViewById(R.id.progresscolor);
        bgColor = findViewById(R.id.bgcolor);
        thumbColor = findViewById(R.id.thumbcolor);

        final AdapterView.OnItemSelectedListener callback = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.startangle) {
                    CircularSeekbar.StartAngle angle = KotlinSampleActivity.getStartAngles().get(position);
                    circularSeekbar.setStartAngle(angle);
                } else if (parent.getId() == R.id.bgcolor) {
                    String color = KotlinSampleActivity.getMaterialColors().get(position);
                    circularSeekbar.setBackgroundColor(Color.parseColor(color));

                } else if (parent.getId() == R.id.progresscolor) {
                    String color = KotlinSampleActivity.getMaterialColors().get(position);
                    circularSeekbar.setProgressColor(Color.parseColor(color));

                } else if (parent.getId() == R.id.thumbcolor) {
                    String color = KotlinSampleActivity.getMaterialColors().get(position);
                    circularSeekbar.setThumbColor(Color.parseColor(color));
                }
                circularSeekbar.invalidate();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        startAngle.setOnItemSelectedListener(callback);
        progressColor.setOnItemSelectedListener(callback);
        bgColor.setOnItemSelectedListener(callback);
        thumbColor.setOnItemSelectedListener(callback);


        enableTouch = findViewById(R.id.enabletouch);
        showThumb = findViewById(R.id.showthumb);
        CompoundButton.OnCheckedChangeListener checkedChangeListener = (buttonView, isChecked) -> {
            if (buttonView.getId() == R.id.enabletouch) {
                circularSeekbar.setEnableTouch(isChecked);
            } else if (buttonView.getId() == R.id.showthumb) {
                circularSeekbar.setShowThumb(isChecked);
            }
            circularSeekbar.invalidate();
        };
        enableTouch.setOnCheckedChangeListener(checkedChangeListener);
        showThumb.setOnCheckedChangeListener(checkedChangeListener);


        // Comment this to override values defined in the xml
        updateSeekbarValues();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                circularSeekbar.setAnimatedProgress(75f, 600L);
            }
        }, 800);

        findViewById(R.id.button).setVisibility(View.GONE);

    }

    private void updateSeekbarValues() {
        bgColor.setSelection(8);
        progressColor.setSelection(1);
        thumbColor.setSelection(1);
        startAngle.setSelection(2);

        thickness.setProgress(100);
        thumbRadius.setProgress(100);

        showThumb.setChecked(true);
        enableTouch.setChecked(true);
    }
}
