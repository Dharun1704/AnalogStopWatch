package com.example.analogclock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class AnalogClock extends View {

    private int height = 0, width = 0, padding  = 0;
    private int[] secNumber = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60};
    private int[] minNumber = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private int secClockRadius = 0, minClockRadius = 0;
    private int numberSpacing = 0;
    private int minHandLength = 0, secHandLength;
    private boolean isStarted;

    private Paint paint;
    private Rect rect = new Rect();

    public AnalogClock(Context context) {
        super(context);
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void startClock() {
        height = getHeight();
        width = getWidth();

        padding = numberSpacing + 40;
        int minimum = Math.min(height, width);
        secClockRadius = minimum / 2 - padding + 8 ;
        minClockRadius = minimum / 5 - padding;
        minHandLength = minimum / 20;
        secHandLength = minimum / 8;
        paint = new Paint();
        isStarted = true;
        MainActivity.isReset = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isStarted)
            startClock();

        canvas.drawColor(Color.parseColor("#252525"));
        drawSecClockBorder(canvas);
        drawMinClockBorder(canvas);
        writeSecNumbers(canvas);
        writeMinNumbers(canvas);
        drawHands(canvas,0,0);
        drawSecHandJunction(canvas);
        drawMinHandJunction(canvas);

        if (!MainActivity.isReset) {
            postInvalidateDelayed(1000);
            invalidate();
        }
        else {
            drawHands(canvas, 0 ,0);
        }

    }

    private void drawHands(Canvas canvas, int sec, int min) {


        if (!MainActivity.isReset) {
            sec = MainActivity.aSec;
            min = MainActivity.aMin;
            drawSecHand(canvas, sec);
            drawMinHand(canvas, min);

        }
        if (MainActivity.isReset) {
            sec = 0;
            min = 0;
            drawSecHand(canvas, sec);
            drawMinHand(canvas, min);
            invalidate();
        }

    }

    private void drawSecClockBorder(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle((float) width / 2, (float) height / 2, secClockRadius + padding - 10, paint);
    }

    private void drawMinClockBorder(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle((float) width / 2, (float) (height / 3.8), minClockRadius + padding - 10, paint);
    }

    private void writeSecNumbers(Canvas canvas) {
        paint.setColor(Color.WHITE);
        int a = 1;
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                14, getResources().getDisplayMetrics()));
        paint.setStrokeWidth(2);
        for (int n : secNumber) {
            String val = String.valueOf(n);
            paint.getTextBounds(val, 0, val.length(), rect);
            double positionAngle = Math.PI / 6 * (a - 3);
            a++;
            int x = (int) (width / 2 + (Math.cos(positionAngle) * secClockRadius) - rect.width() / 2);
            int y = (int) (height / 2 + (Math.sin(positionAngle) * secClockRadius) + rect.height() / 2);
            canvas.drawText(val, x, y, paint);
        }
    }

    private void writeMinNumbers(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                9, getResources().getDisplayMetrics()));
        paint.setStrokeWidth(2);
        for (int n : minNumber) {
            String val = String.valueOf(n);
            paint.getTextBounds(val, 0, val.length(), rect);
            double positionAngle = Math.PI / 6 * (n - 3);
            int start = (int) (width / 2 + (Math.cos(positionAngle) * minClockRadius) - rect.width() / 2);
            int end = (int) ((height / 3.8) + (Math.sin(positionAngle) * minClockRadius) + rect.height() / 2);
            canvas.drawText(val, start, end, paint);
        }
    }

    private void drawSecHand(Canvas canvas, double time) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        double angle = Math.PI * time / 30 - Math.PI / 2;
        int handLength = secClockRadius - secHandLength;
        canvas.drawLine((float) width / 2, (float) height / 2,
                (float) (width / 2 + Math.cos(angle) * handLength),
                (float) (height / 2 + Math.sin(angle) * handLength),
                paint);
    }

    private void drawMinHand(Canvas canvas, double time) {
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        double angle = Math.PI * time / 30 - Math.PI / 2;
        int handLength = minClockRadius - minHandLength;
        canvas.drawLine((float) width / 2, (float) (height / 3.8),
                (float) (width / 2 + Math.cos(angle) * (handLength)),
                (float) ((height / 3.8) + Math.sin(angle) * (handLength)),
                paint);
    }

    private void drawSecHandJunction(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawCircle((float) width / 2, (float) height / 2, 15, paint);
    }

    private void drawMinHandJunction(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawCircle((float) width / 2, (float) (height / 3.8), 15, paint);
    }
}
