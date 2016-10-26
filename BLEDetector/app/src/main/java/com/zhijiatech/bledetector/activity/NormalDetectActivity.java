package com.zhijiatech.bledetector.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
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
import com.zhijiatech.bledetector.util.DescriptionUtil;
import com.zhijiatech.bledetector.util.MathUtils;
import com.zhijiatech.bledetector.util.ScreenUtils;
import com.zhijiatech.bledetector.view.LineWave;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.zhijiatech.bledetector.constant.MConstants.REFRESH.O_START_TIMER;
import static com.zhijiatech.bledetector.constant.MConstants.REFRESH.O_STOP_TIMER;

public class NormalDetectActivity extends BaseActivity {
    private Button mBtNormalStartDetect;
    private Button mBtNormalStopDetect;
    private LinearLayout mLlPreDetect;
    private Timer mStartTimer, mStopTimer;
    private float mDeltaHeight;
    private int mWaveHeightIndex;
    private TextView mTvTimer;
    private LineWave mTimerWave;
    private RelativeLayout mRlNormalDetectFinish;
    private ImageButton mIbBackArrowPre, mIbBackArrowFinish;
    private Button mBtRedoDetect;

    private TextView mTvValuePm25;
    private TextView mTvValueCo2;
    private TextView mTvDesPm25;
    private TextView mTvDesCo2;
    private TextView mTvPreDetectTypeName,mTvDetectFinishTypeName;

    private BLEService mBLEService;

    private ArrayList<Integer> mValuePM25List;
    private ArrayList<Integer> mValueCO2List;

    private int mValuePm25 = 0;
    private int mValueCo2 = 0;

    private int mCountPm25 = 0;
    private int mCountCo2 = 0;

    private String mDetectType;

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
        setContentView(R.layout.activity_normal_detect);
        mDeltaHeight = (ScreenUtils.getScreenHeight(this) * 9.0f / 10.0f) / 3000.0f;
        mWaveHeightIndex = 0;
        mValuePM25List = new ArrayList<>();
        mValueCO2List = new ArrayList<>();
        Intent intent =getIntent();
        mDetectType = intent.getStringExtra("normal_detect_type");
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

                    mTimerWave.setWaveHeight(mDeltaHeight * (mWaveHeightIndex++));
                    mTvTimer.setText("" + mWaveHeightIndex / 100);
                } else {
                    clearStartTimer();
                    calculateValue();
                    mRlNormalDetectFinish.setVisibility(View.VISIBLE);
                    updateView();
                }
                break;

            case O_STOP_TIMER:
                if (mWaveHeightIndex > 0) {
                    mTimerWave.setWaveHeight(mDeltaHeight * (mWaveHeightIndex -= 2));
                    mTvTimer.setText("" + mWaveHeightIndex / 50);
                } else {
                    clearStopTimer();
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
        mTvValuePm25.setText(mValuePm25+"");
        mTvValueCo2.setText(mValueCo2+"");
        mTvDesPm25.setText(DescriptionUtil.despPm25(mValuePm25));
        mTvDesCo2.setText(DescriptionUtil.despCO2(mValueCo2));
    }

    private void calculateValue() {
        mValuePm25 = MathUtils.getAverageValue(mValuePM25List, mCountPm25);
        mValueCo2 = MathUtils.getAverageValue(mValueCO2List, mCountCo2);
    }

    private void startTimer() {
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

    private void clearStartTimer(){
        if (mStartTimer!=null){
            mStartTimer.cancel();
            mStartTimer = null;
        }
    }

    private void clearStopTimer(){
        if (mStopTimer!=null){
            mStopTimer.cancel();
            mStopTimer=null;
        }
    }

    private void initComponents() {
        mTvPreDetectTypeName = (TextView) findViewById(R.id.tv_pre_normal_detect_type_name);
        mTvDetectFinishTypeName = (TextView) findViewById(R.id.tv_normal_detect_finish_type_name);

        if (getResources().getString(R.string.detect_type_o_en).equals(mDetectType)){
            mTvPreDetectTypeName.setText(getResources().getString(R.string.detect_type_1));
            mTvDetectFinishTypeName.setText(getResources().getString(R.string.detect_type_1));
        }
        else if (getResources().getString(R.string.detect_type_i_en).equals(mDetectType)){
            mTvPreDetectTypeName.setText(getResources().getString(R.string.detect_type_2));
            mTvDetectFinishTypeName.setText(getResources().getString(R.string.detect_type_2));
        }
        mIbBackArrowPre = (ImageButton) findViewById(R.id.ib_normal_detect_back_arrow);
        mIbBackArrowFinish = (ImageButton) findViewById(R.id.iv_normal_detect_finish_back_arrow);
        mBtRedoDetect = (Button) findViewById(R.id.outdoor_redo_normal_detect);

        mIbBackArrowPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mIbBackArrowFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtRedoDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWaveHeightIndex = 0;
                clearData();
                startTimer();
                mRlNormalDetectFinish.setVisibility(View.GONE);
            }
        });

        mRlNormalDetectFinish = (RelativeLayout) findViewById(R.id.rl_layout_detect_finish);
        mTvTimer = (TextView) findViewById(R.id.tv_normal_detect_timer_count);
        mLlPreDetect = (LinearLayout) findViewById(R.id.ll_pre_detect_bg);

        mBtNormalStartDetect = (Button) findViewById(R.id.bt_normal_detect_button);
        mBtNormalStopDetect = (Button) findViewById(R.id.bt_stop_normal_detect_button);

        mBtNormalStartDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtNormalStopDetect.setVisibility(View.VISIBLE);
                mLlPreDetect.setVisibility(View.GONE);
                mTvTimer.setVisibility(View.VISIBLE);
                clearData();
                startTimer();
            }
        });

        mBtNormalStopDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlPreDetect.setVisibility(View.VISIBLE);
                mBtNormalStopDetect.setVisibility(View.GONE);
                mTvTimer.setVisibility(View.GONE);
                clearData();
                stopTimer();
                mStartTimer.cancel();
            }
        });

        mTimerWave = (LineWave) findViewById(R.id.lw_time_wave);

        mTvValuePm25 =(TextView) findViewById(R.id.tv_normal_detect_finish_value_pm25);
        mTvValueCo2 =(TextView) findViewById(R.id.tv_normal_detect_finish_value_co2);
        mTvDesPm25 =(TextView) findViewById(R.id.tv_normal_detect_finish_description_pm25);
        mTvDesCo2 =(TextView) findViewById(R.id.tv_normal_detect_finish_description_co2);
    }

    private void clearData() {
        mValuePM25List.clear();
        mValueCO2List.clear();
        mCountPm25 = 0;
        mCountCo2 = 0;
    }

    @Override
    protected void onDestroy() {
        clearStartTimer();
        clearStopTimer();
        if (mServiceConnection!=null){
            unbindService(mServiceConnection);
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i("---------","OActivityStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i("---------","OActivityPause");
        super.onPause();
    }
}
