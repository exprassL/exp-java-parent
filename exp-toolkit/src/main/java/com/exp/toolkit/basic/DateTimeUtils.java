package com.exp.toolkit.basic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间转换器，
 */
public final class DateTimeUtils {
    
    private static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HMS = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter YMD_HMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 今天，仅日期
     *
     * @return  返回今天的日期字符串，仅日期
     */
    public static String today() {
        return LocalDate.now().format(YMD);
    }
    
    /**
     * 现在，仅时间
     *
     * @return  返回现在的时间字符串
     */
    public static String moment() {
        return LocalDateTime.now().format(HMS);
    }
    
    /**
     * 现在，日期时间
     *
     * @return  返回现在的日期时间字符串
     */
    public static String now() {
        return LocalDateTime.now().format(YMD_HMS);
    }
    
    /**
     * 现在，指定格式
     *
     * @param pattern 日期时间格式
     * @return  返回现在日期时间指定格式的字符串，
     */
    public static String now(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * 指定{@link Date}对象按指定格式转字符串
     *
     * @param date    指定{@link Date}对象
     * @param pattern 日期时间格式
     * @return  返回指定Date对象指定格式的字符串
     */
    public static String localDateTime(Date date, String pattern) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * 指定{@link Date}对象按{@link #YMD_HMS yyyy-MM-dd HH:mm:ss}格式转字符串
     *
     * @param date  指定{@link Date}对象
     * @return  指定Date对象yyyy-MM-dd HH:mm:ss格式的字符串
     */
    public static String localDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(YMD_HMS);
    }
    
    /**
     * 指定{@link Date}对象按{@link #YMD yyyy-MM-dd}格式转字符串，仅日期
     *
     * @param date  指定{@link Date}对象
     * @return  指定Date对象yyyy-MM-dd格式的字符串
     */
    public static String localDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(YMD);
    }
    
    /**
     * 指定{@link Date}对象按{@link #HMS HH:mm:ss}格式转字符串
     *
     * @param date 指定{@link Date}对象
     * @return  指定Date对象HH:mm:ss格式的字符串
     */
    public static String localTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().format(HMS);
    }
    
    /**
     * 日期时间字符串按指定格式转{@link LocalDateTime}对象
     *
     * @param text    日期时间字符串
     * @param pattern   指定格式
     * @return  LocalDateTime对象
     */
    public static LocalDateTime localDateTime(String text, String pattern) {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * {@link #YMD_HMS}格式日期时间字符串转{@link LocalDateTime}对象
     *
     * @param text 日期时间字符串，{@link #YMD_HMS}格式
     * @return  LocalDateTime对象
     */
    public static LocalDateTime localDateTime(String text) {
        return LocalDateTime.parse(text, YMD_HMS);
    }
    
    /**
     * 日期字符串按指定格式转{@link LocalDate}对象
     *
     * @param text    日期字符串
     * @param pattern   指定格式
     * @return  LocalDate对象
     */
    public static LocalDate localDate(String text, String pattern) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * {@link #YMD}格式日期字符串转{@link LocalDate}对象
     *
     * @param text {@link #YMD}格式日期字符串
     * @return  LocalDate对象
     */
    public static LocalDate localDate(String text) {
        return LocalDate.parse(text, YMD);
    }
    
    /**
     * 时间字符串按指定格式转{@link LocalTime}对象
     *
     * @param text  时间字符串
     * @param pattern 指定格式
     * @return  LocalTime对象
     */
    public static LocalTime localTime(String text, String pattern) {
        return LocalTime.parse(text, DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * {@link #HMS}格式时间字符串转{@link LocalTime}对象
     *
     * @param text  {@link #HMS}格式时间字符串
     * @return  LocalTime对象
     */
    public static LocalTime localTime(String text) {
        return LocalTime.parse(text, HMS);
    }
    
    /**
     * 日期时间字符串按指定格式转{@link Date}对象
     *
     * @param text    日期时间字符串
     * @param pattern 指定格式
     * @return  Date对象
     */
    public static Date toDate(String text, String pattern) {
        return Date.from(localDateTime(text, pattern).atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * 日期时间字符串按{@link #YMD_HMS}格式转{@link Date}对象，但时间部分舍弃
     *
     * @param text    日期时间字符串
     * @param pattern   指定格式
     * @return  Date对象，时间部分为00:00:00
     */
    public static Date toDateOnly(String text, String pattern) {
        return Date.from(localDateTime(text, pattern).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * 日期时间字符串按{@link #YMD}格式转{@link Date}对象，并时间部分舍弃
     *
     * @param text 期时间字符串
     * @return  Date对象，时间部分为00:00:00
     */
    public static Date toDateOnly(String text) {
        return Date.from(localDate(text).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
