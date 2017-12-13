package com.learn.liyv.learnbilibili.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.learn.liyv.learnbilibili.LearnBilibiliApp;

/**
 * Created by lyh on 2017/12/8.
 * SP缓存工具
 */

public class PreferenceUtil {


    public static boolean getBoolean(String key, boolean defValue) {

        LearnBilibiliApp context = LearnBilibiliApp.getmInstance();
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defValue);
    }

    public static void remove(String... keys) {
        if (keys != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                    LearnBilibiliApp.getmInstance());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (String key : keys) {
                editor.remove(key);
            }
            editor.apply();
        }
    }
}
