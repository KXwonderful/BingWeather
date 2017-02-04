package com.kxwon.bingweather.db;

import org.litepal.crud.DataSupport;

/**
 * Function：县实体类
 * Author：kxwon on 2017/2/1 15:36
 * Email：kxwonder@163.com
 */

public class County extends DataSupport {

    private int id;

    private String countyName;// 县的名字

    private String weatherId;// 县所对应的天气id

    private int cityId;// 当前县所属的市的id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
