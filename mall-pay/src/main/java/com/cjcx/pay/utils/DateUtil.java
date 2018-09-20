package com.cjcx.pay.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class DateUtil {

    /**
     * 当前的时区
     */
    public static final ZoneId zoneId = ZoneId.systemDefault();

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


    /**
     * LocalDateTime 转换 字符
     * @param date
     * @param pattern
     * @return
     */
    public static String localDateTimeToStr(LocalDateTime date, String pattern) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(pattern);
        return f.format(date);
    }

    /**
     * 字符 转换 LocalDateTime
     * @param str
     * @param pattern
     * @return
     */
    public static LocalDateTime toLocalDateTime(String str, String pattern) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(str, f);
    }

    /**
     * Date 转换 LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), zoneId);
    }

    /**
     * LocalDateTime 转换 Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /**
     * 字符 转 Date
     * @param str
     * @param pattern
     * @return
     */
    public static Date strToDate(String str, String pattern) {
        return localDateTimeToDate(toLocalDateTime(str, pattern));
    }

    /**
     * Date 转  字符
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToStr(Date date, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(dateToLocalDateTime(date));
    }


    /**
     * 日期 增加 减少
     *
     * @param date
     * @param field
     * @param days
     * @return
     */
    public static Date add(Date date, int field, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, days);
        return c.getTime();
    }

    public static void main(String[] args) {
        //显示时区
        Set<String> set = ZoneId.getAvailableZoneIds();
        set.forEach(System.out::println);

        LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        System.out.println(ldt);

        LocalDateTime ldt2 = LocalDateTime.now(ZoneId.of("Etc/GMT+9"));
        System.out.println(ldt2);

        LocalDateTime ldt3 = LocalDateTime.now(ZoneId.systemDefault());
        System.out.println(ldt3);

        System.out.println(ZoneId.systemDefault().toString());
    }
}
