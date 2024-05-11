package com.example.movieboxoffice.spider.detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Component
public class MovieDetailSpider {

    @Autowired
    private MovieDetailPageProcessor spiderPageProcessor;
    @Autowired
    private MovieDetailPipeline pipeline;


    private static String url = "https://baike.baidu.com/item/";


    public  Spider getDefaultSpider(String movieName){
        return Spider.create(spiderPageProcessor).addPipeline(pipeline).addUrl(url+movieName).thread(5);
    }





}
