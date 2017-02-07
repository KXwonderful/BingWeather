package com.kxwon.bingweather.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Function：日期工具类
 * Author：kxwon on 2017/2/7 11:15
 * Email：kxwonder@163.com
 */

public class DateUtils {

    /**
     * 判断当前日期是星期几
     * @param date  如"2016-08-30"
     * @return
     */
    public static String StringData(String date){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String mWeek = "";
        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            mWeek ="天";
        }else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
            mWeek ="一";
        }else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
            mWeek ="二";
        } else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
            mWeek ="三";
        }else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
            mWeek ="四";
        }else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
            mWeek ="五";
        }else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            mWeek ="六";
        }
        return "星期" + mWeek;
    }
}
