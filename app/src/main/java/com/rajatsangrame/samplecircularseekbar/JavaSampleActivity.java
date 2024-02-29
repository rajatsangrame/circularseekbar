package com.rajatsangrame.samplecircularseekbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rajatsangrame.circularseekbar.CircularSeekbar;

public class JavaSampleActivity extends AppCompatActivity {

    private static final String TAG = "JavaSample";

    private CircularSeekbar seekbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(TAG);

        seekbar = findViewById(R.id.circularseekbar);

        // Comment this to override values defined in the xml
        // updateSeekbarValues()

        Log.d(TAG, "onCreate: ${seekbar.getProgress()}");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            seekbar.setAnimatedProgress(75f, 800L);
        }, 800);

        findViewById(R.id.button).setVisibility(View.GONE);

    }

    private void updateSeekbarValues() {
        seekbar.setBackgroundColor(Color.BLACK);
        seekbar.setProgressColor(Color.parseColor("#a5a5a5"));
        seekbar.setThumbColor(Color.parseColor("#f5f5f5"));
        seekbar.setStartAngle(CircularSeekbar.StartAngle.TOP.INSTANCE);
        seekbar.setThickness(24);
        seekbar.setThumbRadius(16);
        seekbar.setThumbPadding(8);
        seekbar.setShowThumb(true);
        seekbar.setEnableTouch(true);
        seekbar.setProgress(20f);
    }
}
