package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Function：GSON 实体类
 * Author：kxwon on 2017/2/1 19:19
 * Email：kxwonder@163.com
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;//温度

    @SerializedName("fl")
    public String influenza; //感冒指数

    @SerializedName("hum")
    public String humidity;//相对湿度

    @SerializedName("pcpn")
    public String precipitation;//降水量


    @SerializedName("vis")
    public String visibility;//能见度

    @SerializedName("cond")
    public More more;  //天气状况

    public Wind wind;  //风力情况

    public class More{

        @SerializedName("txt")
        public String info;//数据详情

        public String code;//天气状况代码
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
