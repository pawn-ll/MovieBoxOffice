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
