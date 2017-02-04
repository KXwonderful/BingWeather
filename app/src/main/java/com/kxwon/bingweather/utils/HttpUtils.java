package com.kxwon.bingweather.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Function：网络请求工具类
 * Author：kxwon on 2017/2/1 15:50
 * Email：kxwonder@163.com
 */

public class HttpUtils {

    /**
     * 用 OKHttp 发送请求
     */
    public static void sendOKHttpRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
