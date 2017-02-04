package com.kxwon.bingweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Function：SharePreference封装
 * Author：kxwon on 2017/2/1 20:27
 * Email：kxwonder@163.com
 */

public class PrefUtils {

    private static final String PREF_NAME = "config";

    /**
     * 读取布尔数据
     * @param ctx 上下文
     * @param key 键
     * @param defaultValue 默认值
     * @return
     */
    public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 添加布尔数据
     * @param ctx 上下文
     * @param key 键
     * @param value 添加的数据
     */
    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 读取字符串
     */
    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * 添加字符串
     */
    public static void setString(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    /**
     * 读取int类型数据
     */
    public static int getInt(Context ctx, String key, int defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 添加int类型数据
     */
    public static void setInt(Context ctx, String key, int value){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 将数据全部清除掉
     */
    public static void clear(Context ctx){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
