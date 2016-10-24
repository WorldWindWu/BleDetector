package com.zhijiatech.ble.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zhijiatech.ble.R;

import java.io.InputStream;

/**
 * Created by Wujiafeng on 2016/9/27.
 */
public class BatteryStateView extends View {
    private Context mContext;
    private float mWidth;
    private float mHeight;
    private Paint mPaint;
    private float mPowerQuantity = 0.2f;

    public float getPowerQuantity() {
        return mPowerQuantity;
    }

    public void setPowerQuantity(float powerQuantity) {
        mPowerQuantity = powerQuantity;
    }

    public BatteryStateView(Context context) {
        this(context, null);
        mContext = context;
        mPaint = new Paint();
    }

    public BatteryStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
        mPaint = new Paint();
    }

    public BatteryStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算控件尺寸
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap batteryBitmap = ReadBitMap(mContext, R.drawable.battery);
        mWidth=batteryBitmap.getWidth();
        mHeight=batteryBitmap.getHeight();
        mPaint.setColor(Color.WHITE);

        float left = 0.02f * mWidth;
        float top = 0.0f *mHeight;
        float bottom = 0.98f*mHeight;
        float right = left + mPowerQuantity*(0.9f*mWidth);

        canvas.drawRect(left,top,right,bottom,mPaint);
        canvas.drawBitmap(batteryBitmap, 0, 0, mPaint);

    }

    public Bitmap ReadBitMap(Context context, int resId)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;

        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
