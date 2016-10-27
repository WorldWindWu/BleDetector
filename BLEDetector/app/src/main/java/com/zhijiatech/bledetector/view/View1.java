package com.zhijiatech.bledetector.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zhijiatech.bledetector.R;

/**
 * Created by Jiafeng on 2016/10/20.
 */
public class View1 extends View {
    private Paint mPaint1 = new Paint();
    private Paint mPaint2 = new Paint();
    private Paint mPaint3 = new Paint();

    private float mTotalHeight;
    private float mTotalWidth;

    private Context mContext;
    private int[] mValues = new int[]{0};

    public void setValues(int[] values) {
        mValues = values;
    }

    private void initPaint() {
        mPaint1.setColor(mContext.getResources().getColor(R.color.blue_bottom));
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint1.setStrokeWidth(10f);

        mPaint2.setColor(mContext.getResources().getColor(R.color.line_1));
        mPaint2.setStrokeWidth(3);

    }

    public View1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();

        mTotalWidth = getWidth();
        mTotalHeight = getHeight();
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
        canvas.drawARGB(255, 43, 143, 252);
        float h1 = mTotalHeight / 14.0f;
        float h2 = mTotalHeight / 10.0f;
        canvas.drawRect(0.0f, mTotalHeight - h1, mTotalWidth, mTotalHeight, mPaint1);
        canvas.drawLines(new float[]{
                0,mTotalHeight-h1-h2,mTotalWidth,mTotalHeight-h1-h2,
                0,mTotalHeight-h1-2*h2,mTotalWidth,mTotalHeight-h1-2*h2,
                0,mTotalHeight-h1-3*h2,mTotalWidth,mTotalHeight-h1-3*h2,
                0,mTotalHeight-h1-4*h2,mTotalWidth,mTotalHeight-h1-4*h2,
                0,mTotalHeight-h1-5*h2,mTotalWidth,mTotalHeight-h1-5*h2
        },mPaint2);
    }
}
