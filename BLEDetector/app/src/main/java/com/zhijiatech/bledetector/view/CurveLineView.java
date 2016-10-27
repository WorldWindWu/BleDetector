package com.zhijiatech.bledetector.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Jiafeng on 2016/10/20.
 */
public class CurveLineView extends View {
    private static final String TAG = "CurveLineView";
    private Paint mFieldPaint, mCurvePaint;
    private Paint mLinePaint;
    private Paint mCircleSPaint;
    private Paint mCircleLPaint;

    private Path mFieldPath;

    private float mTotalHeight;
    private float mTotalWidth;

    private float mDeltaX;
    private float mDeltaY;

    private float mLineCircleX;
    private float mLineCircleY;

    private float[] mLines;

    private int mSize;
    private int mMaxValue;

    private int mIndex;

    private int[] mValues = new int[]{1};

    private boolean isMoving;

    public float getLineCircleX() {
        return mLineCircleX;
    }

    public void setLineCircleX(float lineCircleX) {
        mLineCircleX = lineCircleX;
    }

    public float getLineCircleY() {
        return mLineCircleY;
    }

    public void setLineCircleY(float lineCircleY) {
        mLineCircleY = lineCircleY;
    }

    public void updateValue(int[] values, int maxValue, int size) {
        mValues = values;
        mMaxValue = maxValue;
        mSize = size;
    }

    public CurveLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    private void initData() {
        mFieldPaint = new Paint();
        mFieldPaint.setAntiAlias(true);
        mFieldPaint.setColor(0xFFFF0000);

        mCurvePaint = new Paint();
        mCurvePaint.setAntiAlias(true);
        mCurvePaint.setStyle(Paint.Style.FILL);
        mCurvePaint.setColor(Color.WHITE);
        mCurvePaint.setStrokeWidth(10f);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(0x99FFFFFF);
        mLinePaint.setStrokeWidth(4f);

        mCircleSPaint = new Paint();
        mCircleSPaint.setAntiAlias(true);
        mCircleSPaint.setStyle(Paint.Style.FILL);
        mCircleSPaint.setColor(Color.WHITE);

        mCircleLPaint = new Paint();
        mCircleLPaint.setAntiAlias(true);
        mCircleLPaint.setStyle(Paint.Style.FILL);
        mCircleLPaint.setColor(0x55FFFFFF);

        mFieldPath = new Path();

        mSize = mValues.length;
        mMaxValue = mValues[0];

        isMoving = false;
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTotalWidth = getMeasuredWidth();
        mTotalHeight = getMeasuredHeight();
        // Log.i("------Total-----",mTotalWidth+"---"+mTotalHeight+"--"+((mTotalWidth * 7.0f )/ 8.0f ));

    }

    private void updateValues(Canvas canvas) {
        mDeltaY = (mTotalHeight / 2.0f) / (mMaxValue * 1.0f);
        mDeltaX = ((mTotalWidth * 7.0f) / 8.0f) / (mSize * 1.0f);

        // Log.i("------Delta-----",mDeltaX+"---"+mDeltaY);
        mFieldPath.reset();
        mFieldPath.lineTo(0, mTotalHeight / 2.0f - mTotalHeight / 14.0f);

        mLines = new float[(mSize + 1) * 4];

        mLines[0] = 0;
        mLines[1] = mTotalHeight / 2.0f - mTotalHeight / 14.0f;

        for (int i = 1; i <= mSize; i++) {
            //Log.i("------XY-----",mDeltaX * i+"---"+(mTotalHeight - mTotalHeight / 14.0f - mDeltaY * mValues[i-1]));
            mFieldPath.lineTo(mDeltaX * i, mTotalHeight - mTotalHeight / 14.0f - mDeltaY * mValues[i - 1]);
            mLines[4 * i - 2] = mDeltaX * i;
            mLines[4 * i - 1] = mTotalHeight - mTotalHeight / 14.0f - mDeltaY * mValues[i - 1];
            mLines[4 * i - 0] = mDeltaX * i;
            mLines[4 * i + 1] = mTotalHeight - mTotalHeight / 14.0f - mDeltaY * mValues[i - 1];
            canvas.drawCircle(mDeltaX * i,mTotalHeight - mTotalHeight / 14.0f - mDeltaY * mValues[i - 1],4,mCurvePaint);
        }

        mLines[4 * mSize + 2] = mTotalWidth;
        mLines[4 * mSize + 3] = mTotalHeight - mTotalHeight / 14.0f - mDeltaY * mValues[mSize - 1];
        mFieldPath.lineTo(mTotalWidth, mTotalHeight - mTotalHeight / 14.0f - mDeltaY * mValues[mSize - 1]);
        mFieldPath.lineTo(mTotalWidth, mTotalHeight - mTotalHeight / 14.0f);
        mFieldPath.lineTo(0, mTotalHeight - mTotalHeight / 14.0f);
        mFieldPath.lineTo(0, mTotalHeight / 2.0f - mTotalHeight / 14.0f);

        if (isMoving) {
            mLineCircleX = (mLines[mIndex * 4 + 0]);
            mLineCircleY = (mLines[mIndex * 4 + 1]);
        } else {
            mLineCircleX = (mLines[mSize * 4 + 0]);
            mLineCircleY = (mLines[mSize * 4 + 1]);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        updateValues(canvas);

        int count = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        super.draw(canvas);
        canvas.drawLines(mLines, mCurvePaint);

        if (Build.VERSION.SDK_INT >= 14) {
            mFieldPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawPath(mFieldPath, mFieldPaint);
        } else {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            (new Canvas(bitmap)).drawPath(mFieldPath, mFieldPaint);
            mFieldPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(bitmap, 0, 0, mFieldPaint);
            mFieldPaint.setXfermode(null);
        }
        canvas.restoreToCount(count);
        canvas.drawCircle(getLineCircleX(), getLineCircleY(), 20, mCircleSPaint);
        canvas.drawCircle(getLineCircleX(), getLineCircleY(), 40, mCircleLPaint);
        canvas.drawLine(getLineCircleX(), 0, getLineCircleX(), mTotalHeight - mTotalHeight / 14.0f, mLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "---onTouchEvent action:ACTION_DOWN");
                if (Math.abs(mLineCircleX - x) < mDeltaX){
                    isMoving = true;
                    mIndex = mSize;
                }
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "---onTouchEvent action:ACTION_MOVE");
                mIndex = (int)(x / mDeltaX);
                if (mIndex > mSize){
                    mIndex = mSize;
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                Log.d(TAG, "---onTouchEvent action:ACTION_UP");
                isMoving = false;
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "---onTouchEvent action:ACTION_CANCEL");

                break;
        }
        return true;
    }

}

