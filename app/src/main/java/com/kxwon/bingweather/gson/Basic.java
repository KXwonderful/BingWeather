package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Function：GSON 实体类
 * Author：kxwon on 2017/2/1 19:10
 * Email：kxwonder@163.com
 */

public class Basic {

    @SerializedName("city")
    public String cityName;// 城市名

    @SerializedName("cnty")
    public String countryName;// 国家名

    @SerializedName("lat")
    public String latitude;// 纬度

    @SerializedName("lon")
    public String longitude;// 经度

    @SerializedName("prov")
    public String province;// 省份

    @SerializedName("id")
    public String weatherId;// 城市对应的天气id

    public Update update;

    // 更新时间
    public class Update{
        @SerializedName("loc")
        public String locateTime;// 当地时间

        @SerializedName("utc")
        public String utcTime; // utc时间
    }
}
