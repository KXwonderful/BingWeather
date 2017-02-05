package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Function：GSON 实体类
 * Author：kxwon on 2017/2/1 19:25
 * Email：kxwonder@163.com
 */

public class DayForecast {

    public String date;

    @SerializedName("hum")
    public String humidity;//相对湿度

    @SerializedName("pcpn")
    public String precipitation;//降水量

    public String pop;//降水概率

    public String pres;//气压

    @SerializedName("vis")
    public String visibility;//能见度

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;   //天气状况

    public Wind wind;

    public class Temperature{

        public String max;

        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info_d; //白天天气状况

        @SerializedName("txt_n")
        public String info_n; //夜间天气状况

        public String code_n; //夜间天气状况代码

        public String code_d; //夜间天气状况代码
    }

    public class Wind{

        @SerializedName("deg")
        public String windDirection;//风向（360度）

        @SerializedName("dir")
        public String direction;//风向

        @SerializedName("sc")
        public String windScale;//风力等级

        @SerializedName("spd")
        public String windSpeed;//风速

    }
}
