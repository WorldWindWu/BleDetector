package com.zhijiatech.ble.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhijiatech.ble.R;
import com.zhijiatech.ble.util.ScreenUtils;
import com.zhijiatech.ble.view.LineWave;
import com.zhijiatech.ble.view.PopupButton;

import java.util.Timer;
import java.util.TimerTask;

public class OutdoorAirActivity extends BaseActivity {
    private Button mButtonOutdoorDetect;
    private Button mButtonOutdoorStopDetect;
    private PopupButton mPopupButton;
    private LinearLayout mLinearLayoutDetectOutdoorBg;
    private View mViewParent;
    private Timer mStartTimer,mStopTimer;
    private float mDetaHeight;
    private int mWaveHeightIndex;
    private TextView mTextViewTimer;
    private LineWave mTimerWave;
    private RelativeLayout mLayoutOutdoorFinished;
    private ImageButton mImageButtonBack1,mImageButtonBack2;
    private Button mButtonReDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        mViewParent = layoutInflater.inflate(R.layout.activity_outdoor_air,null);
        setContentView(R.layout.activity_outdoor_air);
        mDetaHeight = (ScreenUtils.getScreenHeight(this)*9.0f/10.0f)/3000.0f;
        mWaveHeightIndex=0;
        initComponents();
    }

    private void startTimer() {
        mStartTimer = new Timer();
        mStartTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mWaveHeightIndex<3000) {
                    mTimerWave.setWaveHeight(mDetaHeight * (mWaveHeightIndex++));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextViewTimer.setText(""+mWaveHeightIndex/100);
                        }
                    });
                }else {
                    mStartTimer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLayoutOutdoorFinished.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        },0,10);
    }

    private void stopTimer() {
        mStopTimer = new Timer();
        mStopTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mWaveHeightIndex>0) {
                    mTimerWave.setWaveHeight(mDetaHeight * (mWaveHeightIndex-=2));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextViewTimer.setText(""+mWaveHeightIndex/50);
                        }
                    });
                }else {
                    mStopTimer.cancel();
                }
            }
        },0,1);
    }

    private void initComponents() {
        mImageButtonBack1 = (ImageButton) findViewById(R.id.detecting_outdoor_back);
        mImageButtonBack2 = (ImageButton) findViewById(R.id.detected_outdoor_back);
        mButtonReDetect = (Button) findViewById(R.id.outdoor_re_detect);

        mImageButtonBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mImageButtonBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mButtonReDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWaveHeightIndex=0;
                startTimer();
                mLayoutOutdoorFinished.setVisibility(View.GONE);
            }
        });

        mLayoutOutdoorFinished = (RelativeLayout) findViewById(R.id.layout_outdoor_air_finished);
        mTextViewTimer = (TextView) findViewById(R.id.timer_count);
        mLinearLayoutDetectOutdoorBg = (LinearLayout) findViewById(R.id.detect_outdoor_bg);

        mButtonOutdoorDetect = (Button) findViewById(R.id.outdoor_detect_button);
        mButtonOutdoorStopDetect = (Button) findViewById(R.id.outdoor_stop_detect_button);

        mButtonOutdoorDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonOutdoorStopDetect.setVisibility(View.VISIBLE);
                mLinearLayoutDetectOutdoorBg.setVisibility(View.GONE);
                mTextViewTimer.setVisibility(View.VISIBLE);
                startTimer();

            }
        });

        mButtonOutdoorStopDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayoutDetectOutdoorBg.setVisibility(View.VISIBLE);
                mButtonOutdoorStopDetect.setVisibility(View.GONE);
                mTextViewTimer.setVisibility(View.GONE);
                stopTimer();
                mStartTimer.cancel();
            }
        });

        mTimerWave = (LineWave) findViewById(R.id.time_wave);
    }
}
