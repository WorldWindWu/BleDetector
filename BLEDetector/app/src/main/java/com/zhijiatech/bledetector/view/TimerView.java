package com.zhijiatech.bledetector.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jiafeng on 2016/10/20.
 */
public class TimerView extends View {
    private float mWidth,mHeight;
    private Paint mPaintStroke1,mPaintStroke2;
    private Paint mPaintTitle,mPaintContent1,mPaintContent2;
    private float mBase = 1.0f;
    private boolean mIsFinish;
    private int mMins,mSecs;

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintStroke1 = new Paint();
        mPaintStroke1.setColor(0xFFE1F0FC);
        mPaintStroke1.setAntiAlias(true);
        mPaintStroke1.setStyle(Paint.Style.STROKE);

        mPaintStroke2 = new Paint();
        mPaintStroke2.setColor(0xFF7BC9FF);
        mPaintStroke2.setAntiAlias(true);
        mPaintStroke2.setStyle(Paint.Style.STROKE);

        mPaintTitle = new Paint();
        mPaintTitle.setAntiAlias(true);
        mPaintTitle.setColor(0xFFA4B8D4);

        mPaintContent1 = new Paint();
        mPaintContent1.setAntiAlias(true);
        mPaintContent1.setColor(0xFF2985ED);

        mPaintContent2 = new Paint();
        mPaintContent2.setAntiAlias(true);
        mPaintContent2.setColor(0xFF2985ED);
        mMins = 20;
        mSecs = 00;
        mIsFinish = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mBase = mHeight / 338.0f;
        mPaintStroke1.setStrokeWidth(15*mBase);
        mPaintStroke2.setStrokeWidth(9*mBase);
        mPaintTitle.setTextSize(26.0f*mBase);

        mPaintContent1.setTextSize(31.0f*mBase);
        mPaintContent2.setTextSize(23.0f*mBase);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mWidth/2.0f,mHeight/2.0f,mBase*110, mPaintStroke1);
        canvas.drawCircle(mWidth/2.0f,mHeight/2.0f,mBase*100,mPaintStroke2);

        if (mIsFinish){

        }else{
            canvas.drawText("剩余时间",mWidth/2.0f-48.0f*mBase,mHeight/2.0f - 29*mBase,mPaintTitle);
            canvas.drawText(String.format("%02d",mMins),mWidth/2.0f-62.0f*mBase,mHeight/2.0f + 31*mBase,mPaintContent1);
            canvas.drawText("分",mWidth/2.0f-26.0f*mBase,mHeight/2.0f + 34*mBase,mPaintContent2);
            canvas.drawText(String.format("%02d",mSecs),mWidth/2.0f,mHeight/2.0f + 31*mBase,mPaintContent1);
            canvas.drawText("秒",mWidth/2.0f+44.0f*mBase,mHeight/2.0f + 34*mBase,mPaintContent2);
        }
    }
}
