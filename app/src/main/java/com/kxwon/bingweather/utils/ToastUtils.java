package com.kxwon.bingweather.utils;

import android.widget.Toast;

import com.kxwon.bingweather.app.MyApplication;

/**
 * Function：弹吐司工具类
 * Author：kxwon on 2017/2/1 12:09
 * Email：kxwonder@163.com
 */

public class ToastUtils {

    // Toast
    private static Toast toast;

    public ToastUtils() {
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 短时间显示Toast
     * @param resId
     */
    public static void showShort(int resId) {
        showShort(MyApplication.getInstance().getString(resId));
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param resId
     */
    public static void showLong(int resId) {
        showLong(MyApplication.getInstance().getString(resId));
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        if (null == toast) {
            toast = Toast.makeText(MyApplication.getInstance(), message, duration);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param resId
     * @param duration
     */
    public static void show(int resId, int duration) {
        show(MyApplication.getInstance().getString(resId),duration);
    }

    /**
     * Hide the toast, if any.
     */
    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }
}
