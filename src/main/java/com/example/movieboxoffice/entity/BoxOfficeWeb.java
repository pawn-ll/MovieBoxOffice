package com.example.movieboxoffice.entity;

import lombok.Data;

@Data
public class BoxOfficeWeb {


//    电影代码
    private String code ;
//    电影名称
    private String name ;
//    含服务费总票房
    private String sumSalesDesc;
//    总分账票房
    private String sumSplitSalesDesc;
//    已上映时间
    private String releaseDays ;
//    当天含服务费票房
    private String salesInWanDesc;
//    当天分账票房
    private String splitSalesInWanDesc;
//    当天票房占比
    private String salesRateDesc;
//    当天分账票房占比
    private String splitSalesRateDesc;
//    当天排片率
    private String sessionRateDesc;
//    当天上座率
    private String seatRateDesc;
//    线上售票占比
    private String onlineSalesRateDesc;
//    线上分账占比
    private String splitOnlineSalesRateDesc;







}
