package com.zhijiatech.bledetector.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Jiafeng on 2016/10/26.
 */

public class BlurUtils {
    public static void applyBlur(final Context context, final View view1, final View view2) {
        view1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view1.getViewTreeObserver().removeOnPreDrawListener(this);
                view1.buildDrawingCache();
                view1.setDrawingCacheEnabled(true);
                Bitmap bitmap = view1.getDrawingCache();
                blur(bitmap, view2,context);
                return true;
            }
        });
    }

    private static void blur(Bitmap bkg, View view, Context context) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 12;
        float radius = 2;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int)radius, true);
        view.setBackground(new BitmapDrawable(context.getResources(), overlay));
    }
}
