package com.zhijiatech.bledetector.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;

import com.zhijiatech.bledetector.MainLogic.MainLogic;

import java.util.List;
import java.util.UUID;

/**
 * Created by Jiafeng on 2016/10/17.
 */
public class BleUtil {
    private static final String LIST_NAME = "NAME";
    private static final String LIST_UUID = "UUID";

    /**
     * 20.    * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     * 21.
     */
    public static int bytesToInt2(byte[] src) {
        int value;
        value = (int) (((src[0] & 0xFF) << 8) | (src[1] & 0xFF));
        return value;
    }

    public static void parse(byte[] data, UUID uuid, Context context){
        if (uuid.equals(UUID.fromString(SampleGattAttributes.VALUE_PM25_MEASUREMENT))){
            MainLogic.getInstance().setValuePM25(bytesToInt2(data));
            context.sendBroadcast(new Intent(BLEReceiver.ACTION_GET_DATA_PM25));
        }
        else if(uuid.equals(UUID.fromString(SampleGattAttributes.VALUE_CO2_MEASUREMENT))){
            MainLogic.getInstance().setValueCO2(bytesToInt2(data));
            context.sendBroadcast(new Intent(BLEReceiver.ACTION_GET_DATA_CO2));
        }
        else if(uuid.equals(UUID.fromString(SampleGattAttributes.VALUE_BATTERY_MEASUREMENT))){
            MainLogic.getInstance().setBleBattery(bytesToInt2(data));
            context.sendBroadcast(new Intent(BLEReceiver.ACTION_GET_DATA_BATTERY));
        }
    }

    public static void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
         // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.equals(SampleGattAttributes.VALUE_PM25_MEASUREMENT)){
                    MainLogic.getInstance().setBluetoothGattCharacteristicPm25(gattCharacteristic);
                }else if (uuid.equals(SampleGattAttributes.VALUE_CO2_MEASUREMENT)){
                    MainLogic.getInstance().setBluetoothGattCharacteristicCo2(gattCharacteristic);
                }else if (uuid.equals(SampleGattAttributes.VALUE_BATTERY_MEASUREMENT)){
                    MainLogic.getInstance().setBluetoothGattCharacteristicBattery(gattCharacteristic);
                }else if (uuid.equals(SampleGattAttributes.DEVICE_STATE_MEASUREMENT)){
                    MainLogic.getInstance().setBluetoothGattCharacteristicState(gattCharacteristic);
                }else if (uuid.equals(SampleGattAttributes.NOTIFY_SERIAL_DATA)){
                    MainLogic.getInstance().setBluetoothGattCharacteristicNotify(gattCharacteristic);
                }
            }
        }
    }
}
