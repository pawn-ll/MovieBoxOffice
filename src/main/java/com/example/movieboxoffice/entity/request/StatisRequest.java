package com.example.movieboxoffice.entity.request;

import lombok.Data;

@Data
public class StatisRequest {

    /**
     * 0-自定义 1-月 2-年
     */
    public Integer statisType;

    /**
     * 统计区间（2024-01 or 2024）
     */
    public String statisInterval;

    public String startDate;

    public String endDate;


}
