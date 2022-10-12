package com.zhibo8.game.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhibo8.game.sdk.base.ZB8Constant;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8SPUtils {

    private static SharedPreferences sharedPreferences;


    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(ZB8Constant.SP_NAME, Context.MODE_PRIVATE);
    }

    public static String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public static boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }


    public static void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }


    public static void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }
}
