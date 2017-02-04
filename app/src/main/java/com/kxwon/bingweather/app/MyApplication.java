package com.kxwon.bingweather.app;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Function：全局类
 * Author：kxwon on 2017/2/1 09:33
 * Email：kxwonder@163.com
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // 调用 LitePal 的初始化方法
        LitePal.initialize(this);

    }

    /**
     * Singleton main method. Provides the global static instance of the helper class.
     * @return The MyApplication instance.
     */
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
