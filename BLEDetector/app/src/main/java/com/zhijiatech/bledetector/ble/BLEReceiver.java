package com.zhijiatech.bledetector.ble;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhijiatech.bledetector.activity.BaseActivity;
import com.zhijiatech.bledetector.constant.MConstants.BLE;

/**
 * Created by Jiafeng on 2016/10/24.
 */

public class BLEReceiver extends BroadcastReceiver {
    public static final String ACTION_GET_DATA_PM25             = "com.zhijiatech.bledetector.value_pm25";//得到PM25数据
    public static final String ACTION_GET_DATA_CO2              = "com.zhijiatech.bledetector.value_co2";//得到CO2数据
    public static final String ACTION_GET_DATA_BATTERY             = "com.zhijiatech.bledetector.value_battery";//得到CO2数据
    public static final String ACTION_BLE_CONNECT               = "com.zhijiatech.bledetector.connect";//蓝牙连接
    public static final String ACTION_BLE_DISCONNECT            = "com.zhijiatech.bledetector.disconnect";//蓝牙断开
    public static final String ACTION_DATA_AVAILABLE            = "com.zhijiatech.bledetector.data_available";//获得数据
    public static final String ACTION_GATT_SERVICES_DISCOVERED  = "com.zhijiatech.bledetector.gatt_services_discovered";//获得服务特征
    public static final String ACTION_NOTIFY_SHOP               = "com.mx.hwb.notify_data";//获得连续数据

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == intent)
        {
            return;
        }

        BaseActivity ba = null;

        if (context instanceof BaseActivity)
        {
            ba = (BaseActivity) context;
        }

        if (ACTION_GET_DATA_PM25.equals(intent.getAction())){//获得PM2.5数据
            ba.sendHandlerMessage(BLE.GET_VALUE_PM25,null);
        }else if(ACTION_GET_DATA_CO2.equals(intent.getAction())){//获得CO2数据
            ba.sendHandlerMessage(BLE.GET_VALUE_CO2,null);
        }else if(ACTION_GET_DATA_BATTERY.equals(intent.getAction())){//获得电量
            ba.sendHandlerMessage(BLE.GET_VALUE_BATTERY,null);
        }else if(ACTION_BLE_CONNECT.equals(intent.getAction())){
            ba.sendHandlerMessage(BLE.CONNECT_SUCCESS,null);
        }else if(ACTION_BLE_DISCONNECT.equals(intent.getAction())){

        }else if(ACTION_DATA_AVAILABLE.equals(intent.getAction())){

        }else if(ACTION_GATT_SERVICES_DISCOVERED.equals(intent.getAction())){
            ba.sendHandlerMessage(BLE.GATT_SERVICES_DISCOVERED,null);
        }else if(ACTION_NOTIFY_SHOP.equals(intent.getAction())){

        }else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){
            int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            switch (bluetoothState){
                case BluetoothAdapter.STATE_ON:
                    ba.sendHandlerMessage(BLE.BLUETOOTH_STATE_ON,null);
                    break;

                case BluetoothAdapter.STATE_OFF:

                    break;
            }
        }else if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(intent.getAction())){
            int bleConnectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE,0);
            switch (bleConnectionState){
                case BluetoothAdapter.STATE_CONNECTED:

                    break;
                case BluetoothAdapter.STATE_DISCONNECTED:

                    break;
            }
        }
    }
}
