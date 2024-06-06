package com.example.movieboxoffice.entity.request;

import lombok.Data;

@Data
public class StatisRequest {

    public Integer statisType;

    public String statisInterval;

    public String startDate;

    public String endDate;


}
