package com.example.movieboxoffice.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyDateUtils {

    public static final String YY = "yyyy";
    public static final String YYMM = "yyyy-MM";
    public final static String YYMMDD = "yyyy-MM-dd";
    public final static String YYMMDDHHMMSS = "yyyy-MM-dd hh:mm:ss";

    public static void main(String[] args) throws ParseException {
        System.out.println(getNowDate(YYMMDD));
    }

    public static String getNowDate (){
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat(YYMMDDHHMMSS);
        return dateFormat.format(calendar.getTime());
    }

    public static String getNowStringDate (String pattern){
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat(pattern);
        return dateFormat.format(calendar.getTime());
    }
    public static String getStringDate (Date date ,String pattern){
        SimpleDateFormat dateFormat= new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static Date getNowDate (String pattern) throws ParseException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(sdf.format(date));

    }
    public static String getTodayAddDate(String pattern, int addDay){
        return getAddDate(getNowStringDate(pattern),pattern,addDay);
    }

    /**
     * 根据给定的日期字符串、日期格式和要增加的天数，返回一个新的日期字符串。
     * @param date 传入的日期字符串
     * @param pattern 日期字符串的格式
     * @param addDay 需要增加的天数，可以为负数以表示减少天数
     * @return 格式化后的日期字符串
     */
    public static String getAddDate(String date ,String pattern,int addDay){
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String endDateStr = "";
        try {
            Date startDate = formatter.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, addDay); // 加上x天

            endDateStr = formatter.format(calendar.getTime()); // 格式化日期

        } catch (Exception e) {
            e.printStackTrace();
        }
        return endDateStr;
    }

    /**
     * 生成指定年月的所有日期字符串，格式为yyyyMMdd。
     *
     * @param year  年份
     * @param month 月份
     * @return 该月所有日期的字符串列表
     */
    public static List<String> generateDatesOfYearMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<String> dates = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dates.add(date.toString());
        }

        return dates;
    }

    public static Date parseDate(String date ,String pattern){
        Date resultDate = null;
        try {
            resultDate = DateUtils.parseDate(date, pattern);
        }catch (Exception e){

        }
        return resultDate;
    }



}
