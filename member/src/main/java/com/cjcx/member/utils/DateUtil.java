package com.cjcx.member.utils;

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

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static SimpleDateFormat DF_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static String convertToStr(LocalDateTime date) {
        return formatter.format(date);
    }

    public static String convertToStr(LocalDateTime date, String pattern) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(pattern);
        return f.format(date);
    }

    public static LocalDateTime convertToDate(String str, String pattern) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(str, f);
    }

    /**
     * Date 转换 LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), zoneId);
    }

    /**
     * LocalDateTime 转换 Date
     *
     * @param localDateTime
     * @return
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
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
