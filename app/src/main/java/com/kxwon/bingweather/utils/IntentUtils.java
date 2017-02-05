package com.kxwon.bingweather.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * activity跳转方法类
 * Created by KXwon on 2016/6/27.
 */
public class IntentUtils {

    /**
     * activity跳转类
     * 调用：IntentUtils.myIntent(context,xxx.class)
     * @param context
     * @param cls
     */
    public static void myIntent(Context context, Class cls){
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
    }

    /**
     * activity传递参数跳转类
     * @param context
     * @param cls
     * @param string
     * @param ID
     */
    public static void myIntent(Context context, Class cls, String string, int ID){
        Intent intent = new Intent(context,cls);
        intent.putExtra(string,ID);
        context.startActivity(intent);
    }

    /**
     * activity传递参数跳转类
     * @param context
     * @param cls
     * @param string
     * @param ID
     */
    public static void myIntentString(Context context, Class cls, String string, String ID){
        Intent intent = new Intent(context,cls);
        intent.putExtra(string,ID);
        context.startActivity(intent);
    }


    /**
     * activity传递两个参数跳转类
     * @param context
     * @param cls
     * @param s1
     * @param s2
     * @param id1
     * @param id2
     */
    public static void myIntent(Context context, Class cls, String s1, String s2, int id1, int id2){
        Intent intent = new Intent(context,cls);
        intent.putExtra(s1,id1);
        intent.putExtra(s2,id2);
        context.startActivity(intent);
    }

    /**
     * activity通过bundle传值
     * @param context
     * @param cls
     * @param s1 要传的值的标记
     * @param s2 需要传的值
     * @param s3 bundle名
     * @param s4
     * @param id
     */
    public static void myIntent(Context context, Class cls, String s1, String s2, String s3, String s4, int id){
        Intent intent = new Intent(context,cls);
        Bundle bundle = new Bundle();
        bundle.putString(s1,s2);
        intent.putExtra(s3,bundle);
        intent.putExtra(s4,id);
        context.startActivity(intent);
    }
}
