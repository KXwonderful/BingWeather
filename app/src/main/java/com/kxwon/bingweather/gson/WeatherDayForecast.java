package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Function：7-10天天气预报
 * Author：kxwon on 2017/2/7 13:17
 * Email：kxwonder@163.com
 */

public class WeatherDayForecast {

    public String status;

    public Basic basic;

    @SerializedName("daily_forecast")
    public List<DayForecast> dayForecastList;
}
