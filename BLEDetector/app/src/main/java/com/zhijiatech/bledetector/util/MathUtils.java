package com.zhijiatech.bledetector.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jiafeng on 2016/10/26.
 */

public class MathUtils {
    public static int getAverageValue(ArrayList<Integer> data, int size) {
        int value = 0;
        int totalValue = 0;
        Integer[] integers = data.toArray(new Integer[size]);
        Arrays.sort(integers);
        for (int i = 0; i < size - 1; i++) {
            totalValue += integers[i];
        }

        value = totalValue / size - 2;
        return value;
    }
}
