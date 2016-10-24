package com.zhijiatech.ble.ble;

/**
 * Created by Jiafeng on 2016/10/17.
 */
public class BleUtil {
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }

        int v = 0;
        for (int i = 0; i < src.length; i++) {
            v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 20.    * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     * 21.
     */
    public static int bytesToInt2(byte[] src) {
        int value;
        value = (int) (((src[0] & 0xFF) << 8) | (src[1] & 0xFF));
        return value;
    }

    public static String ByteArraytoHex(byte[] bytes) {
        if(bytes!=null){
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02X ", b));
            }
            return sb.toString();
        }
        return "";
    }
}
