package com.example.movieboxoffice.entity;

import lombok.Data;

@Data
public class DoubanSuggest {

//            "episode": "",
//            "img": "https://img3.doubanio.com\/view\/photo\/s_ratio_poster\/public\/p2400082527.jpg",
//            "title": "长城",
//            "url": "https:\/\/movie.douban.com\/subject\/6982558\/?suggest=%E9%95%BF%E5%9F%8E",
//            "type": "movie",
//            "year": "2016",
//            "sub_title": "The Great Wall",
//            "id": "6982558"
    private String episode;
    private String img;
    private String title;
    private String url;
    private String type;
    private String year;
    private String sub_title;
    private String id;



}
