package com.zhijiatech.ble.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zhijiatech.ble.R;
import com.zhijiatech.ble.ble.BluetoothLeService;
import com.zhijiatech.ble.fragment.LeftMainFragment;
import com.zhijiatech.ble.fragment.RightMainFragment;
import com.zhijiatech.ble.view.BatteryStateView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private BatteryStateView mBatteryStateView;
    private ImageButton mTipImageView;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentPagerAdapter mFragmentPagerAdapter;

    private LeftMainFragment mLeftMainFragment;
    private RightMainFragment mRightMainFragment;

    private Button mLeftCircleButton,mRightCircleButton;
    private FrameLayout mTipLayout;
    private ViewPager mViewPager;

    private BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD = 10000;
    private Handler mHandler;
    private boolean mScanning;
    private ArrayList<BluetoothDevice> mBluetoothDevices;
    private int mMaxRssi = -100;
    private BluetoothDevice mBluetoothDeviceConnect;
    private BluetoothLeService mBluetoothLeService;

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    if (device.getName().equals("DM2.5")) {
                        if (rssi > mMaxRssi) {
                            mMaxRssi = rssi;
                            mBluetoothDeviceConnect = device;
                        }
                    }
                }
            };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();

        initComponents();
        initFragments();
        initBle();
    }

    private void initBle(){
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Toast.makeText(this,"本机不支持蓝牙！",Toast.LENGTH_SHORT).show();
        }
        finish();

        final BluetoothManager bluetoothManager =
                (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null){
            Toast.makeText(this, "本机不支持蓝牙！", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        scanLeDevice(true);
    }



    private void scanLeDevice(final boolean enable){
        if (enable){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    connectBleDevice();
                }
            },SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }else{
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private void connectBleDevice(){
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent,mServiceConnection,BIND_AUTO_CREATE);
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mBluetoothDeviceConnect.getAddress());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    private void initComponents(){
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

        mBatteryStateView = (BatteryStateView)findViewById(R.id.battery);
        mTipImageView = (ImageButton)findViewById(R.id.tip);

        mTipImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTipLayout.setVisibility(View.VISIBLE);
            }
        });

        mTipLayout = (FrameLayout)findViewById(R.id.tip_layout);

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
                if (position==0){
                    mLeftCircleButton.setBackgroundResource(R.drawable.small_circle_checked);
                    mRightCircleButton.setBackgroundResource(R.drawable.small_circle_unchecked);
                }else{
                    mLeftCircleButton.setBackgroundResource(R.drawable.small_circle_unchecked);
                    mRightCircleButton.setBackgroundResource(R.drawable.small_circle_checked);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
