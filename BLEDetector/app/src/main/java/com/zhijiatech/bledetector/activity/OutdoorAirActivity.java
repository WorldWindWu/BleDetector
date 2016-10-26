package com.zhijiatech.bledetector.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhijiatech.bledetector.MainLogic.MainLogic;
import com.zhijiatech.bledetector.R;
import com.zhijiatech.bledetector.ble.BLEReceiver;
import com.zhijiatech.bledetector.ble.BLEService;
import com.zhijiatech.bledetector.constant.MConstants;
import com.zhijiatech.bledetector.util.MathUtils;
import com.zhijiatech.bledetector.util.ScreenUtils;
import com.zhijiatech.bledetector.view.LineWave;
import com.zhijiatech.bledetector.view.PopupButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.zhijiatech.bledetector.constant.MConstants.REFRESH.O_START_TIMER;
import static com.zhijiatech.bledetector.constant.MConstants.REFRESH.O_STOP_TIMER;

public class OutdoorAirActivity extends BaseActivity {
    private Button mButtonOutdoorDetect;
    private Button mButtonOutdoorStopDetect;
    private PopupButton mPopupButton;
    private LinearLayout mLinearLayoutDetectOutdoorBg;
    private View mViewParent;
    private Timer mStartTimer, mStopTimer;
    private float mDetaHeight;
    private int mWaveHeightIndex;
    private TextView mTextViewTimer;
    private LineWave mTimerWave;
    private RelativeLayout mLayoutOutdoorFinished;
    private ImageButton mImageButtonBack1, mImageButtonBack2;
    private Button mButtonReDetect;

    private TextView mValuePM25TextView;
    private TextView mValueCO2TextView;
    private TextView mDesPM25TextView;
    private TextView mDesCO2TextView;

    private BLEService mBLEService;

    private ArrayList<Integer> mValuePM25List;
    private ArrayList<Integer> mValueCO2List;

    private int mValuePm25 = 0;
    private int mValueCo2 = 0;

    private int mCountPm25 = 0;
    private int mCountCo2 = 0;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBLEService = ((BLEService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBLEService = null;
        }
    };

    @Override
    public void init() {
        initData();
        initComponents();
        connectBleDevice();
    }

    private void connectBleDevice() {
        Intent gattServiceIntent = new Intent(this, BLEService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void initData() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        mViewParent = layoutInflater.inflate(R.layout.activity_outdoor_air, null);
        setContentView(R.layout.activity_outdoor_air);
        mDetaHeight = (ScreenUtils.getScreenHeight(this) * 9.0f / 10.0f) / 3000.0f;
        mWaveHeightIndex = 0;
    }

    @Override
    public void register() {
        mBLEReceiver = new BLEReceiver();
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BLEReceiver.ACTION_GATT_SERVICES_DISCOVERED);
            filter.addAction(BLEReceiver.ACTION_BLE_CONNECT);
            filter.addAction(BLEReceiver.ACTION_GET_DATA_PM25);
            filter.addAction(BLEReceiver.ACTION_GET_DATA_CO2);
            registerReceiver(mBLEReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void baseHandleMessage(Message msg) {
        switch (msg.what) {
            case O_START_TIMER:
                if (mWaveHeightIndex < 3000) {
                    if (mWaveHeightIndex % 100 == 0) {
                        mBLEService.readCharacteristic(MainLogic.getInstance().getBluetoothGattCharacteristicPm25());
                    } else if (mWaveHeightIndex % 100 == 50) {
                        mBLEService.readCharacteristic(MainLogic.getInstance().getBluetoothGattCharacteristicCo2());
                    }

                    mTimerWave.setWaveHeight(mDetaHeight * (mWaveHeightIndex++));
                    mTextViewTimer.setText("" + mWaveHeightIndex / 100);
                } else {
                    mStartTimer.cancel();
                    calculateValue();
                    mLayoutOutdoorFinished.setVisibility(View.VISIBLE);
                    updateView();
                }
                break;

            case O_STOP_TIMER:
                if (mWaveHeightIndex > 0) {
                    mTimerWave.setWaveHeight(mDetaHeight * (mWaveHeightIndex -= 2));
                    mTextViewTimer.setText("" + mWaveHeightIndex / 50);
                } else {
                    mStopTimer.cancel();
                }
                break;

            case MConstants.BLE.GET_VALUE_PM25:
                mValuePm25 = MainLogic.getInstance().getValuePM25();
                mValuePM25List.add(new Integer(mValuePm25));
                mCountPm25++;
                break;

            case MConstants.BLE.GET_VALUE_CO2:
                mValueCo2 = MainLogic.getInstance().getValueCO2();
                mValueCO2List.add(new Integer(mValueCo2));
                mCountCo2++;
                break;
        }
    }

    private void updateView() {
        mValuePM25TextView.setText(mValuePm25+"");
        mValueCO2TextView.setText(mValueCo2+"");
    }

    private void calculateValue() {
        mValuePm25 = MathUtils.getAverageValue(mValuePM25List, mCountPm25);
        mValueCo2 = MathUtils.getAverageValue(mValueCO2List, mCountCo2);
    }

    private void startTimer() {
        mValuePM25List = new ArrayList<>();
        mValueCO2List = new ArrayList<>();

        mStartTimer = new Timer();
        mStartTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(O_START_TIMER);
            }
        }, 0, 10);
    }

    private void stopTimer() {
        mStopTimer = new Timer();
        mStopTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(O_STOP_TIMER);
            }
        }, 0, 1);
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
                mWaveHeightIndex = 0;
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

        mValuePM25TextView=(TextView) findViewById(R.id.value_pm25);
        mValueCO2TextView=(TextView) findViewById(R.id.value_co2);
        mDesPM25TextView=(TextView) findViewById(R.id.desp_pm25);
        mDesCO2TextView=(TextView) findViewById(R.id.desp_co2);
    }
}
