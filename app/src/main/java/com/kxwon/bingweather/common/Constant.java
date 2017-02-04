package com.kxwon.bingweather.common;

import com.kxwon.bingweather.gson.SearchCity;

/**
 * Function：常量
 * Author：kxwon on 2017/2/1 16:39
 * Email：kxwonder@163.com
 */

public class Constant {

    public class Pref{
        public static final String FIRST_START = "is_first_start";// 是否第一次启动
        public static final String WEATHER = "weather";// 天气
        public static final String BRING_PIC = "bing_pic";// 必应图片
    }

    // 省市县
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    // 获取地区url
    public static final String URL_ADDRESS_BASE = "http://guolin.tech/api/china";
    // 获取查询城市url（和风api）
    public static final String URL_CITY = "https://free-api.heweather.com/v5/search?city=";

    // 获取必应每日一图url
    public static final String URL_BEING_PIC = "http://guolin.tech/api/bing_pic";

    // 获取天气url
    public static final String URL_WEATHER_BASE = "https://free-api.heweather.com/v5/";
    public static final String URL_WEATHER = "https://free-api.heweather.com/v5/weather";
    public static final String URL_WEATHER_KEY = "7a19535621fe46f9ad4b6491b3970b98";

    // 天气 id
    public static final String WEATHER_ID = "weather_id";
    public static final String WEATHER_CITY = "weather_city";


}
