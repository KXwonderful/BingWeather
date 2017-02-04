package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Function：GSON 实体类
 * Author：kxwon on 2017/2/1 19:25
 * Email：kxwonder@163.com
 */

public class HourForecast {

    public String date;


    @SerializedName("hum")
    public String humidity;//相对湿度

    public String pop;//降水概率

    public String pres;//气压

    @SerializedName("tmp")
    public String temperature;//温度

    @SerializedName("cond")
    public More more;

    public Wind wind;

    public class More{
        @SerializedName("txt")
        public String info;

        public String code;  //天气状况代码
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
