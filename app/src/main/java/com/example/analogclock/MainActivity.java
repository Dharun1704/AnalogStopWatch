package com.example.analogclock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    AnalogClock clock;

    Button start, reset;
    boolean isRunning;

    public static boolean isReset = false;
    public static int aSec, aMin;

    long tMillis = 0L, tStart = 0L, tBuff = 0L, tUpdate = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(Color.parseColor("#252525"));

        handler = new Handler();
        clock = findViewById(R.id.clock);
        start = findViewById(R.id.stopWatchStart);
        reset = findViewById(R.id.stopWatchReset);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReset = false;
                if (!isRunning) {
                    tStart = SystemClock.uptimeMillis();
                    isRunning = true;
                    clock = new AnalogClock(MainActivity.this);
                    handler.postDelayed(updateTime, 0);
                    start.setText("Pause");
                    reset.setVisibility(View.GONE);
                }
                else {
                    tBuff += tMillis;
                    handler.removeCallbacks(updateTime);
                    isRunning = false;
                    start.setText("Start");
                    reset.setVisibility(View.VISIBLE);
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMillis = 0L;
                tStart = 0L;
                tBuff = 0L;
                tUpdate = 0L;
                isReset = true;
                reset.setVisibility(View.GONE);
            }
        });
    }



    private Runnable updateTime = new Runnable() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void run() {
            tMillis = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff + tMillis;

            int sec = (int) (tUpdate / 1000);
            int min = sec / 60;
            sec %= 60;

            aSec = sec;
            aMin = min * 5;

            handler.postDelayed(updateTime, 1000);
        }
    };
}