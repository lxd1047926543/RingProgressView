package com.example.richard.ringprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RingProgressView ringProgressView = (RingProgressView) findViewById(R.id.ring_progress_view);
        ringProgressView.setPresetTheme(RingProgressView.PresetTheme.THEME_BLUE);
        ringProgressView.setFinalProgress(90.f);
        ringProgressView.setBaseRingRadius(500.0f);
        ringProgressView.setShadowWidth(300);
        ringProgressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ringProgressView.startAnimation();
            }
        }, 2000);
    }
}
