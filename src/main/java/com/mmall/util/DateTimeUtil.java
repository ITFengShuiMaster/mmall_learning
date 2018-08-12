package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @author Luyue
 * @date 2018/7/31 17:33
 **/
public class DateTimeUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串时间转date
     *
     * @param date
     * @return
     */
    public static Date strToDate(String date) {
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(DATE_FORMAT);
        DateTime dateTime = dateTimeFormat.parseDateTime(date);
        return dateTime.toDate();
    }

    /**
     * date时间转字符串
     *
     * @param date
     * @return
     */
    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        DateTime dateTime = new DateTime(date);
        return dateTime.toString(DATE_FORMAT);
    }

    /***
     * 自定义转换格式
     * @param date
     * @param dateFormat
     * @return
     */
    public static Date strToDate(String date, String dateFormat) {
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(dateFormat);
        DateTime dateTime = dateTimeFormat.parseDateTime(date);
        return dateTime.toDate();
    }

    /**
     * 自定义转换格式
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateToStr(Date date, String dateFormat) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        DateTime dateTime = new DateTime(date);
        return dateTime.toString(dateFormat);
    }

    public static void main(String[] args) {
        System.out.println(strToDate("2017-11-11 11:11:11", DATE_FORMAT));
        System.out.println(dateToStr(new Date(), DATE_FORMAT));
    }
}
