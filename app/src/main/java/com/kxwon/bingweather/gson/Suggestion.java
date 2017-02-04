package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Function：GSON 实体类
 * Author：kxwon on 2017/2/1 19:21
 * Email：kxwonder@163.com
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;//舒适度指数

    @SerializedName("cw")
    public CarWash carWash;//洗车指数

    @SerializedName("drsg")
    public Dress dress;//穿衣指数

    @SerializedName("flu")
    public Cold cold;//感冒指数

    public Sport sport;

    @SerializedName("trav")
    public Travel travel;//旅游指数

    public Uv uv;   //紫外线指数

    public class Comfort{
        @SerializedName("txt")
        public String info; //数据详情

        @SerializedName("brf")
        public String brief; //简介
    }

    public class CarWash{
        @SerializedName("txt")
        public String info; //数据详情

        @SerializedName("brf")
        public String brief; //简介
    }

    public class Sport{
        @SerializedName("txt")
        public String info; //数据详情

        @SerializedName("brf")
        public String brief; //简介
    }

    public class Dress{
        @SerializedName("txt")
        public String info; //数据详情

        @SerializedName("brf")
        public String brief; //简介
    }

    public class Cold{
        @SerializedName("txt")
        public String info; //数据详情

        @SerializedName("brf")
        public String brief; //简介
    }

    public class Travel{
        @SerializedName("txt")
        public String info; //数据详情

        @SerializedName("brf")
        public String brief; //简介
    }

    public class Uv{
        @SerializedName("txt")
        public String info; //数据详情

        @SerializedName("brf")
        public String brief; //简介
    }
}
