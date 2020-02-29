package com.estone.bank.estone_appsmartlock.utils;

import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zhouxinliang on 18-8-20.
 */

public class DateUtils {

    private static  ArrayList<String> dayList = new ArrayList<>();

    private static  String TAG="DateUtils";
    public static long getDayZero(long timestamp) {
        return timestamp / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) {
        long res;
        LUtils.d(TAG,"dateToStamp =="+s);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
            LUtils.d(TAG,"dateToStamp =date ="+date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();

        res = Long.valueOf(ts);
        LUtils.d(TAG,"dateToStamp =res ="+res);
        return res;
    }


    public static  int getDay(int year, int month) {
        int size = dayList.size();
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                for (int i = size; i < 31; i++) {
                    dayList.add(i + 1 + "日");
                }
                break;
            case 2:
                day = flag ? 29 : 28;
                for (int i = day; i < size; i++) {
                    dayList.remove(dayList.size() - 1);
                }
                if (size == 28 && flag) {
                    dayList.add(29 + "日");
                }

                break;
            default:
                day = 30;
                for (int i = size; i < 30; i++) {
                    dayList.add(i + 1 + "日");
                }
                if (size == 31) {
                    dayList.remove(size - 1);
                }
                break;
        }
        return day;
    }
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }



}
