package com.zhijiatech.bledetector.constant;

/**
 * Created by Jiafeng on 2016/10/24.
 */

public class MConstants {
    public interface MGLOBAL{

    }

    public interface BLE{
        int TO_SCAN = 1100;//去搜索
        int CONNECT = 1101;//连接
        int DISCONNECT = 1102;//断开
        int DEVICE_SCAN_OK = 1103;//搜索完成
        int DEVICE_SCAN_TIMEOUT = 1104;//搜索超时
        int BLUETOOTH_STATE_ON = 1005;//蓝牙打开
        int CONNECT_SUCCESS = 1010;
        int GATT_SERVICES_DISCOVERED = 1006;

        int GET_VALUE_PM25 = 1007;
        int GET_VALUE_CO2 = 1008;
        int GET_VALUE_BATTERY = 1009;
    }

    public interface REFRESH{
        int O_START_TIMER = 2001;
        int O_STOP_TIMER =2002;
    }
}
