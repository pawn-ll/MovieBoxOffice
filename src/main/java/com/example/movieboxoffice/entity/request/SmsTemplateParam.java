package com.example.movieboxoffice.entity.request;

import lombok.Data;

@Data
public class SmsTemplateParam {

    //日期
    private String date;
    //总票房
    private String allboxoffice;
    //分账票房
    private String boxoffice;


}
