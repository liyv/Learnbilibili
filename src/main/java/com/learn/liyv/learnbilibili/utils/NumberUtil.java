package com.learn.liyv.learnbilibili.utils;

/**
 * Created by lyh on 2017/12/28 11:02.
 * 1.
 */

public class NumberUtil {

    public static String convertString(int num) {
        if (num < 100000) {
            return String.valueOf(num);
        }

        String unit = "万";
        double newNum = num / 10000.0;
        String numStr = String.format("%." + 1 + "f", newNum);
        return numStr + unit;
    }
}
