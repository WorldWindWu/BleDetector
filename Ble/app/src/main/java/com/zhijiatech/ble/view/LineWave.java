package com.zhijiatech.ble.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zhijiatech.ble.util.ScreenUtils;

/**
 * Created by Jiafeng on 2016/10/14.
 */
public class LineWave extends View {
    private static final int WAVE_PAINT_COLOR = 0xAA2B8FFC;
    private Paint mWavePaint;
    private int mTotalWidth, mTotalHeight;

    private float mWaveHeight;

    public void setWaveHeight(float waveHeight) {
        mWaveHeight = waveHeight;
    }

    public LineWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTotalHeight = (ScreenUtils.getScreenHeight(context)*9)/10;
        mTotalWidth = ScreenUtils.getScreenWidth(context);
        mWaveHeight = 0;
        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint.setColor(WAVE_PAINT_COLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,mTotalHeight-mWaveHeight,mTotalWidth,mTotalHeight,mWavePaint);

        postInvalidateDelayed(30);
    }
}
