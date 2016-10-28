package com.zhijiatech.bledetector.activity;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.zhijiatech.bledetector.R;
import com.zhijiatech.bledetector.view.CurveLineView;
import com.zhijiatech.bledetector.view.TimerView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ContinuousDetectActivity extends BaseActivity {

    private CurveLineView mCurveLineView;
    private TimerView mTimerView;
    private Button mButtonRedo;
    private ImageButton mImageButtonBack;
    private State mState;
    private Button mButtonStart;
    private int mMaxValue = 250;
    private ArrayList<Integer> mIntegers;
    private int value;
    private Timer mTimer;

    private enum State{READY,DETECT,FINISH};
    @Override
    public void init() {
        setContentView(R.layout.activity_continuous_detect);
        mCurveLineView = (CurveLineView) findViewById(R.id.curve_line);
        mButtonStart = (Button) findViewById(R.id.start_continuous_detect);
        mTimerView = (TimerView) findViewById(R.id.continuous_detect_timer);
        mButtonRedo = (Button)findViewById(R.id.outdoor_redo_continuous_detect);
        mImageButtonBack = (ImageButton)findViewById(R.id.iv_continuous_detect_finish_back_arrow);

        mImageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mButtonRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == State.FINISH){
                    stopTimer();
                    startTimer();
                    mState = State.DETECT;
                    mButtonRedo.setText("停止检测");
                }else if (mState == State.DETECT){
                    stopTimer();
                    mState = State.FINISH;
                    mButtonRedo.setText("重测");
                }
            }
        });

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerView.setVisibility(View.VISIBLE);
                mButtonStart.setVisibility(View.GONE);
                startTimer();
                mState = State.DETECT;
                mButtonRedo.setVisibility(View.VISIBLE);
                mButtonRedo.setText("停止检测");
            }
        });

        mTimer = new Timer();
        mIntegers = new ArrayList<>();
        value = 250;
        mState = State.READY;
    }

    private void startTimer() {
        final Random random = new Random();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int temp = random.nextInt(2);
                value = value - temp;
                Log.i("num--", value + "");
                if (value > mMaxValue) mMaxValue = value;
                mIntegers.add(new Integer(value));
                final int[] values = new int[mIntegers.size()];
                for (int i = 0; i < mIntegers.size(); i++) {
                    values[i] = mIntegers.get(i).intValue();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCurveLineView.updateValue(values, mMaxValue, mIntegers.size());
                        mCurveLineView.invalidate();
                    }
                });
            }
        }, 1000, 100);
    }

    @Override
    public void register() {

    }

    @Override
    public void baseHandleMessage(Message msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    private void stopTimer() {
        mIntegers.clear();
        value = 250;
        if (mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }
}
