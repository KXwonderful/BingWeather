package com.kxwon.bingweather.gson;

/**
 * Function：GSON 实体类
 * Author：kxwon on 2017/2/1 19:18
 * Email：kxwonder@163.com
 */

public class AQI {

    public AQICity city;

    public class AQICity{

        public String aqi;

        public String co;

        public String no2;

        public String o3;

        public String pm25;

        public String pm10;

        public String qlty; //共六个级别，分别：优，良，轻度污染，中度污染，重度污染，严重污染

        public String so2;

    }
}
