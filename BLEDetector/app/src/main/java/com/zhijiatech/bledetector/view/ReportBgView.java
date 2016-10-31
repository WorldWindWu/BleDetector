package com.zhijiatech.bledetector.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jiafeng on 2016/10/31.
 */

public class ReportBgView extends View {
    private Path mPath;
    private Paint mPaint;
    private float mWidth, mHeight;

    public ReportBgView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setColor(0xFF2B8FFC);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPath();
        canvas.drawPath(mPath,mPaint);
    }

    private void initPath() {
        mPath.reset();
        mPath.moveTo(0,0);
        mPath.lineTo(0, mHeight * (764.0f / 799.0f));
        mPath.lineTo(mWidth / 2.0f - mHeight * (35.0f / 799.0f), mHeight * (764.0f / 799.0f));
        mPath.lineTo(mWidth / 2.0f, mHeight);
        mPath.lineTo(mWidth / 2.0f + mHeight * (35.0f / 799.0f), mHeight * (764.0f / 799.0f));
        mPath.lineTo(mWidth, mHeight * (764.0f / 799.0f));
        mPath.lineTo(mWidth,0);
        mPath.close();
    }
}
