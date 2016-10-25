package com.zhijiatech.bledetector.activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhijiatech.bledetector.MainLogic.MainLogic;
import com.zhijiatech.bledetector.R;
import com.zhijiatech.bledetector.ble.BLEReceiver;
import com.zhijiatech.bledetector.ble.BLEService;
import com.zhijiatech.bledetector.ble.BleUtil;
import com.zhijiatech.bledetector.constant.MConstants.BLE;
import com.zhijiatech.bledetector.fragment.LeftMainFragment;
import com.zhijiatech.bledetector.fragment.RightMainFragment;
import com.zhijiatech.bledetector.view.BatteryStateView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_ENABLE_BT = 1;

    private BatteryStateView mBatteryStateView;
    private TextView mBatteryValueView;
    private ImageButton mTipImageView;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentPagerAdapter mFragmentPagerAdapter;

    private LeftMainFragment mLeftMainFragment;
    private RightMainFragment mRightMainFragment;

    private Button mLeftCircleButton, mRightCircleButton;
    private FrameLayout mTipLayout;
    private ViewPager mViewPager;

    private BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD = 5000;
    private boolean mScanning;
    private ArrayList<BluetoothDevice> mBluetoothDevices;
    private int mMaxRssi = -100;
    private BluetoothDevice mBluetoothDeviceConnect;
    private BLEService mBLEService;

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    if (device.getName().equals("DM-PM2.5")) {
                        if (rssi > mMaxRssi) {
                            mMaxRssi = rssi;
                            mBluetoothDeviceConnect = device;
                        }
                        Log.i("Device", device.getName() + "-address-" + device.getAddress() + "强度=" + rssi + "==最大" + mBluetoothDeviceConnect.getAddress());
                    }
                }
            };

    @Override
    public void init() {
        setContentView(R.layout.activity_main);
        initComponents();
        initFragments();
        initBle();
    }

    @Override
    public void register() {
        mBLEReceiver = new BLEReceiver();
        try
        {
            IntentFilter filter = new IntentFilter();
            filter.addAction(android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BLEReceiver.ACTION_GATT_SERVICES_DISCOVERED);
            filter.addAction(BLEReceiver.ACTION_BLE_CONNECT);
            filter.addAction(BLEReceiver.ACTION_GET_DATA_BATTERY);
            filter.addAction(BLEReceiver.ACTION_GET_DATA_PM25);
            filter.addAction(BLEReceiver.ACTION_GET_DATA_CO2);
            registerReceiver(mBLEReceiver, filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void baseHandleMessage(Message msg) {
        switch (msg.what){
            case BLE.BLUETOOTH_STATE_ON:
                Log.i("BLESTAE","ON");
                scanLeDevice(true);
                break;

            case BLE.GATT_SERVICES_DISCOVERED://发现服务特征
                BleUtil.displayGattServices(mBLEService.getSupportedGattServices());
                mBLEService.readCharacteristic(MainLogic.getInstance().getBluetoothGattCharacteristicBattery());
                mLeftMainFragment.refreshNowStatus(getResources().getString(R.string.step4_warming_up));
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBLEService.readCharacteristic(MainLogic.getInstance().getBluetoothGattCharacteristicPm25());
                    }
                },10000);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBLEService.readCharacteristic(MainLogic.getInstance().getBluetoothGattCharacteristicCo2());
                    }
                },10500);
                break;

            case BLE.GET_VALUE_BATTERY:
                mBatteryStateView.setPowerQuantity(MainLogic.getInstance().getBleBattery()/100.0f);
                mBatteryValueView.setText(MainLogic.getInstance().getBleBattery()+"%");
                mBatteryStateView.invalidate();
                break;

            case BLE.GET_VALUE_PM25:
                mLeftMainFragment.setNowStatusTextViewInVisible();
                mLeftMainFragment.refreshNowPm25Value(MainLogic.getInstance().getValuePM25()+"");
                break;

            case BLE.GET_VALUE_CO2:
                mLeftMainFragment.setNowStatusTextViewInVisible();
                Log.i("CO2----",MainLogic.getInstance().getValueCO2()+"");
                mLeftMainFragment.refreshNowCO2Value(MainLogic.getInstance().getValueCO2()+"");
                break;

            case BLE.CONNECT_SUCCESS:
                mLeftMainFragment.refreshNowStatus(getResources().getString(R.string.step3_connected_successfully));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }else{
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanLeDevice(true);
                }
            },1000);
        }
    }

    private void initBle() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "本机不支持蓝牙！", Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "本机不支持蓝牙！", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mBluetoothDevices = new ArrayList<>();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mLeftMainFragment.refreshNowStatus(getResources().getString(R.string.step1_opened_and_scaning));
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    connectBleDevice();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private void connectBleDevice() {
        Intent gattServiceIntent = new Intent(this, BLEService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBLEService = ((BLEService.LocalBinder) service).getService();
            if (!mBLEService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBLEService.connect(mBluetoothDeviceConnect.getAddress());
            mLeftMainFragment.refreshNowStatus(getResources().getString(R.string.step2_found_and_connecting));
            Log.i("Device","max=="+mBluetoothDeviceConnect.getAddress()+"");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBLEService = null;
        }
    };

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    private void initComponents() {
        mViewPager = (ViewPager) findViewById(R.id.fragment_viewpager);
        mLeftCircleButton = (Button) findViewById(R.id.small_circle_left);
        mRightCircleButton = (Button) findViewById(R.id.small_circle_right);

        mLeftCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });

        mRightCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });

        mBatteryStateView = (BatteryStateView) findViewById(R.id.battery);
        mBatteryValueView = (TextView)findViewById(R.id.value_battery);

        mTipImageView = (ImageButton) findViewById(R.id.tip);

        mTipImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTipLayout.setVisibility(View.VISIBLE);
            }
        });

        mTipLayout = (FrameLayout) findViewById(R.id.tip_layout);

        mTipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTipLayout.setVisibility(View.GONE);
            }
        });
    }


    private void initFragments() {
        mLeftMainFragment = new LeftMainFragment();
        mRightMainFragment = new RightMainFragment();

        mFragmentList.add(mLeftMainFragment);
        mFragmentList.add(mRightMainFragment);

        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };

        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mLeftCircleButton.setBackgroundResource(R.drawable.small_circle_checked);
                    mRightCircleButton.setBackgroundResource(R.drawable.small_circle_unchecked);
                } else {
                    mLeftCircleButton.setBackgroundResource(R.drawable.small_circle_unchecked);
                    mRightCircleButton.setBackgroundResource(R.drawable.small_circle_checked);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnection!=null){
            unbindService(mServiceConnection);
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        alertbox();
    }

    /**
     * Method to create an alert before user exit from the application
     */
   private void alertbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setMessage(
                getResources().getString(R.string.alert_message_exit))
                .setCancelable(false)
                .setTitle(getResources().getString(R.string.alert_message_exit_title))
                .setPositiveButton(
                        getResources()
                                .getString(R.string.alert_message_exit_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Finish the current activity
                                MainActivity.this.finish();
                                Intent gattServiceIntent = new Intent(getApplicationContext(),
                                        BLEService.class);
                                stopService(gattServiceIntent);

                            }
                        })
                .setNegativeButton(
                        getResources().getString(
                                R.string.alert_message_exit_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
