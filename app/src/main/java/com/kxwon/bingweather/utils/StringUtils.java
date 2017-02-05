package com.kxwon.bingweather.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 字符串工具类
 * Created by KXwon on 2016/7/25.
 */
public class StringUtils {

    /**
     * 去掉字符串中的所有空格，包括首尾、中间
     * @param str
     * @return
     */
    public static String removeAllSpace(String str)
    {
        return str.replace(" ","");
    }

    /**
     * 去掉字符串中的所有逗号，包括首尾、中间
     * @param str
     * @return
     */
    public static String removeAllComma(String str){
        return str.replace(",","");
    }

    /**
     * 将string转化为int
     * @param str
     * @return
     */
    public static int stringToInt(String str){
        try {
            return Integer.parseInt(str) ;
        }catch (NumberFormatException e){
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 将string转化为double
     * @param str
     * @return
     */
    public static double stringToDouble(String str){
        try {
            return Double.parseDouble(str);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return 0.00;
        }
    }

    /**
     * 将int转化为string
     * @param i
     * @return
     */
    public static String intToString(int i){
        return Integer.toString(i);
    }

    /**
     * 将Double转化为String
     * @param d
     * @return
     */
    public static String doubleToString(Double d){
        DecimalFormat format = new DecimalFormat("#.00");
        return format.format(d);
    }

    /**
     * 将float转化为String
     * @param f
     * @return
     */
    public static String floatToString(float f){
        return  String.valueOf(f);
    }

    /**
     * 隐藏手机号码中间4位
     * @param phone 手机号码
     * @return
     */
    public static String formatPhone(String phone){
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 把list对象转化为String
     * @param mList
     * @return
     * @throws IOException
     */
    public static <T>String ObjectToString(List<T> mList)
            throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(mList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String mString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return mString;
    }

    /**
     * 把String转化为list对象
     * @param mString
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> StringToObject(String mString)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(mString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List<T> mList = (List<T>) objectInputStream
                .readObject();
        objectInputStream.close();
        return mList;
    }
}
