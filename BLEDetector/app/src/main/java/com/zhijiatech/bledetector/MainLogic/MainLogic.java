package com.zhijiatech.bledetector.MainLogic;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.ArrayList;

/**
 * Created by WUJIAFENG on 2016/8/31.
 */
public class MainLogic {
    /**
     * double-checked locking
     */
    private volatile static MainLogic sMainLogicInstance;

    private MainLogic(){}

    public static MainLogic getInstance(){
        if (sMainLogicInstance == null){
            synchronized (MainLogic.class){
                if (sMainLogicInstance == null){
                    sMainLogicInstance = new MainLogic();

                }
            }
        }
        return sMainLogicInstance;
    }

    public static void destory(){
        sMainLogicInstance = null;
    }

    private int mValuePM25;


    private int mValueCO2;

    private ArrayList<Integer> valuePM25s;
    private ArrayList<Integer> valueCO2s;

    private int bleBattery;

    private BluetoothGattCharacteristic mBluetoothGattCharacteristicPm25;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristicCo2;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristicBattery;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristicState;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristicNotify;

    public BluetoothGattCharacteristic getBluetoothGattCharacteristicPm25() {
        return mBluetoothGattCharacteristicPm25;
    }

    public void setBluetoothGattCharacteristicPm25(BluetoothGattCharacteristic bluetoothGattCharacteristicPm25) {
        mBluetoothGattCharacteristicPm25 = bluetoothGattCharacteristicPm25;
    }

    public BluetoothGattCharacteristic getBluetoothGattCharacteristicCo2() {
        return mBluetoothGattCharacteristicCo2;
    }

    public void setBluetoothGattCharacteristicCo2(BluetoothGattCharacteristic bluetoothGattCharacteristicCo2) {
        mBluetoothGattCharacteristicCo2 = bluetoothGattCharacteristicCo2;
    }

    public BluetoothGattCharacteristic getBluetoothGattCharacteristicBattery() {
        return mBluetoothGattCharacteristicBattery;
    }

    public void setBluetoothGattCharacteristicBattery(BluetoothGattCharacteristic bluetoothGattCharacteristicBattery) {
        mBluetoothGattCharacteristicBattery = bluetoothGattCharacteristicBattery;
    }

    public BluetoothGattCharacteristic getBluetoothGattCharacteristicState() {
        return mBluetoothGattCharacteristicState;
    }

    public void setBluetoothGattCharacteristicState(BluetoothGattCharacteristic bluetoothGattCharacteristicState) {
        mBluetoothGattCharacteristicState = bluetoothGattCharacteristicState;
    }

    public BluetoothGattCharacteristic getBluetoothGattCharacteristicNotify() {
        return mBluetoothGattCharacteristicNotify;
    }

    public void setBluetoothGattCharacteristicNotify(BluetoothGattCharacteristic bluetoothGattCharacteristicNotify) {
        mBluetoothGattCharacteristicNotify = bluetoothGattCharacteristicNotify;
    }

    public int getBleBattery() {
        return bleBattery;
    }

    public void setBleBattery(int bleBattery) {
        this.bleBattery = bleBattery;
    }

    public void setValueCO2(int valueCO2) {
        mValueCO2 = valueCO2;
    }

    public int getValuePM25() {
        return mValuePM25;
    }

    public void setValuePM25(int valuePM25) {
        mValuePM25 = valuePM25;
    }
    public int getValueCO2() {
        return mValueCO2;
    }
}
