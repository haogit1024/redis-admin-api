package com.czh.redis.common.util;

import javax.validation.constraints.NotNull;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author czh
 * 对jdk LocalDataTime 的简单封装
 */
public class LocalDateTimeUtil {
    /**
     * 东8区
     */
    private static final int TIME_ZONE = 8;
    private static final String ZONE_ID = "Asia/Shanghai";
    private static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static String getStringForFormat(LocalDateTime dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getNowDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(getFormatter());
    }

    public static String getNowTime() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }

    /**
     * 根据天数获取当天开始时间
     * @param days 天数
     * @return 当天开始时间
     */
    public static String getDateTimeStartPlusDays(long days) {
        LocalDate dayDateTime = LocalDate.now().plusDays(days);
        LocalTime monthStartTime = LocalTime.of(0,0,0);
        LocalDateTime monthStartDateTime = LocalDateTime.of(dayDateTime, monthStartTime);
        return monthStartDateTime.format(getFormatter());
    }

    /**
     * 根据天数获取时间
     * @param days 例如 -1: 一天前 1: 一天后
     * @return
     */
    public static String getDateTimePlusDays(long days) {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(days);
        return localDateTime.format(getFormatter());
    }

    /**
     * 根据月数获取时间
     * @param months 例如 -1: 一月前 1: 一月后
     * @return
     */
    public static String getDateTimePlusMonths(long months) {
        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(months);
        return localDateTime.format(getFormatter());
    }

    public static String getDatePlusDays(long days) {
        LocalDate now = LocalDate.now().plusDays(days);
        return now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 获取当前月的 1 号 0 点 0 分 0 秒
     * @return
     */
    public static String getMonthStartDateTime() {
        LocalDate now = LocalDate.now();
        int days = now.getDayOfMonth();
        LocalDate monthStartDate = now.minusDays(days - 1);
        LocalTime monthStartTime = LocalTime.of(0,0,0);
        LocalDateTime monthStartDateTime = LocalDateTime.of(monthStartDate, monthStartTime);
        return monthStartDateTime.format(getFormatter());
    }

    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    public static String getCurrentTime() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        return now.format(formatter);
    }

    public static String getCurrentDateTimeByFormat(String format) {
        LocalTime now = LocalTime.now();
        return now.format(DateTimeFormatter.ofPattern(format));
    }

    public static String getUnionPayCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    /**
     * 根据格式获取字符串中的时间戳
     * @param date
     * @param format
     * @return unix时间戳
     */
    public static long getTimestampByFormat(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        long millis = instant.toEpochMilli();
        return millis / 1000;
    }

    /**
     * 时间格式转换
     * @param sourceDate        源时间字符串
     * @param sourceFormat      源时间格式
     * @param targetFormat      目标时间格式
     * @return
     */
    public static String convertToFormat(String sourceDate, String sourceFormat, String targetFormat) {
        DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern(targetFormat);
        DateTimeFormatter sourceFormatter = DateTimeFormatter.ofPattern(sourceFormat);
        LocalDateTime sourceDateTime = LocalDateTime.parse(sourceDate, sourceFormatter);
        return sourceDateTime.format(targetFormatter);
    }

    /**
     * 时间戳转需要的时间格式
     * @param timestamp     32位unix时间戳
     * @param format        时间格式
     * @return
     */
    public static String timestampConvertToFormat(Long timestamp, String format) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp,0, ZoneOffset.ofHours(TIME_ZONE));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

    public static Date convertToDate(@NotNull LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of(ZONE_ID)).toInstant());
    }
}
