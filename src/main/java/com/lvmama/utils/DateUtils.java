package com.lvmama.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * create by adolph on 2017/12/25
 */

public class DateUtils
{
    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        if(StringUtils.isEmpty(s))
            return "";
        System.out.println(s);
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s)
    {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 当天9点
     * @param date
     * @return
     */
    public static Date day9Hour(Date date)
    {
        return dayXHour(date, 9);
    }

    /**
     * 当天21点
     * @param date
     * @return
     */
    public static Date day21Hour(Date date)
    {
        return dayXHour(date, 21);
    }

    /**
     *
     * @param date
     * @param x
     * @return
     */
    public static Date dayXHour(Date date, int x)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, x);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 下一天
     * @param date
     * @return
     */
    public static Date nextDay(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 计算前多少分的日期
     * @param date
     * @param minute
     * @return
     */
    public static Date dateAddMinute(Date date, int minute)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    /**
     * 计算前多少分的日期
     * @param date
     * @param hour
     * @return
     */
    public static Date dateAddHour(Date date, int hour)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, hour);
        return c.getTime();
    }

    /**
     * 当天的开始时间
     * @param date
     * @return
     */
    public static Date dayStart(Date date)
    {
        return dayXHour(date, 0);
    }

    /**
     * 当天的末尾时间
     * @param date
     * @return
     */
    public static Date dayEnd(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 时间格式化
     * @param date
     * @return
     */
    public static String dateFormatYYYYMMDDHHMMSSSSS(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return format.format(date);
    }

    /**
     * 时间格式化
     * @param date
     * @return
     */
    public static String dateFormatYYYYMMDDHHMMSS(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String dateFormatEEEE(Date date)
    {
        return new SimpleDateFormat("EEEE").format(date);
    }

    public static Date string2date(String dateString)
    {
        Date date = null;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        dateFormat.setLenient(false);
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }
}
