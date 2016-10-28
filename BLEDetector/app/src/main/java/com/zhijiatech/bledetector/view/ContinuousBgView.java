package com.zhijiatech.bledetector.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhijiatech.bledetector.R;

/**
 * Created by Jiafeng on 2016/10/20.
 */
public class ContinuousBgView extends View {
    private Paint mBottomPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;

    private int mMaxValue = 250;

    private float mTotalHeight;
    private float mTotalWidth;

    private Context mContext;

    private float[] mDeltaHeights;

    private void initPaint() {
        mBottomPaint = new Paint();
        mLinePaint = new Paint();
        mTextPaint = new Paint();

        mTextPaint.setColor(Color.WHITE);        // 设置颜色
        mTextPaint.setStyle(Paint.Style.FILL);   // 设置样式
       // mTextPaint.setTextSize(50);// 设置字体大小

        mBottomPaint.setColor(mContext.getResources().getColor(R.color.blue_bottom));
        mBottomPaint.setStyle(Paint.Style.FILL);
        mBottomPaint.setStrokeWidth(10f);

        mLinePaint.setColor(mContext.getResources().getColor(R.color.line_1));
        mLinePaint.setStrokeWidth(3);
    }

    public ContinuousBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTotalWidth = getWidth();
        mTotalHeight = getHeight();
        initPaint();
    }


    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalHeight = h;
        mTotalWidth = w;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mTextPaint.setTextSize(mTotalHeight / 14.0f / 2.5f);// 设置字体大小
        Log.i("------Textsize__",mTotalHeight / 14.0f / 2.0f+"");

        canvas.drawARGB(255, 43, 143, 252);
        float h1 = mTotalHeight / 14.0f;
        float h2 = mTotalHeight / 10.0f;
        mDeltaHeights = new float[5];
        for (int i = 0; i < 5; i++) {
            mDeltaHeights[i] = mTotalHeight-h1-h2 * (i+1);
        }
        canvas.drawRect(0.0f, mTotalHeight - h1, mTotalWidth, mTotalHeight, mBottomPaint);
        canvas.drawLines(new float[]{
                0,mDeltaHeights[0],mTotalWidth,mDeltaHeights[0],
                0,mDeltaHeights[1],mTotalWidth,mDeltaHeights[1],
                0,mDeltaHeights[2],mTotalWidth,mDeltaHeights[2],
                0,mDeltaHeights[3],mTotalWidth,mDeltaHeights[3],
                0,mDeltaHeights[4],mTotalWidth,mDeltaHeights[4]
        }, mLinePaint);

        int deltaY = (int)(mMaxValue / 5.0f+0.5f);
        for (int i = 0; i < 5; i++) {
            canvas.drawText(""+deltaY*(i+1),10,mDeltaHeights[i]+40,mTextPaint);
        }
    }
}
