package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Function：天气预报集合接口 GSON 实体类
 * 包括7-10天预报、实况天气、每小时天气、灾害预警、生活指数、空气质量，一次获取足量数据
 * get接口地址/weather
 * city: string 城市名称 city可通过城市中英文名称、ID和IP地址进行，例如city=北京，city=beijing，city=CN101010100，city= 60.194.130.1
 * key: string 用户认证key
 *
 * Author：kxwon on 2017/2/1 19:28
 * Email：kxwonder@163.com
 */

public class Weather {

    public String status;

    public Alarm alarms;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<DayForecast> dayForecastList;


    @SerializedName("hourly_forecast")
    public List<HourForecast> hourForecastList;
}
