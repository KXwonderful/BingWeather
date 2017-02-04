package com.kxwon.bingweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Function：查询城市 GSON 实体类
 * 若使用模糊查询，则可能返回多个城市数据
 * get 接口地址/search
 * city: string 城市名称 city可通过城市中英文名称、ID和IP地址进行，例如city=北京，city=beijing，city=CN101010100，city= 60.194.130.1
 * key: string 用户认证key
 *
 * Author：kxwon on 2017/2/1 19:25
 * Email：kxwonder@163.com
 */

public class SearchCity {

    @SerializedName("HeWeather5")
    public List<City>cities;

    public class City{

        public String status;

        public Basic basic;

        public class Basic{

            public String city;

            public String cnty;

            public String id;

            public String lat;

            public String lon;

            public String prov;
        }

    }

}
