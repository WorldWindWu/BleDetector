package com.zhijiatech.bledetector.activity;

import android.os.Message;

import com.zhijiatech.bledetector.R;
import com.zhijiatech.bledetector.view.CurveLineView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ContinuousDetectActivity extends BaseActivity {

    private CurveLineView mCurveLineView;
    private int mMaxValue = 0;
    private ArrayList<Integer> mIntegers;

    @Override
    public void init() {
        setContentView(R.layout.activity_continuous_detect);
        mCurveLineView = (CurveLineView) findViewById(R.id.curve_line);
        mIntegers = new ArrayList<>();
        final Random random = new Random();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int temp = random.nextInt(100);
                if (temp > mMaxValue) mMaxValue = temp;
                mIntegers.add(new Integer(temp));
                final int[] values = new int[mIntegers.size()];
                for (int i = 0; i < mIntegers.size(); i++) {
                    values[i] = mIntegers.get(i).intValue();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCurveLineView.updateValue(values,mMaxValue,mIntegers.size());
                        mCurveLineView.invalidate();
                        if (mIntegers.size()>30){
                            timer.cancel();
                        }
                    }
                });
            }
        }, 1000, 1000);
    }

    @Override
    public void register() {

    }

    @Override
    public void baseHandleMessage(Message msg) {

    }
}
