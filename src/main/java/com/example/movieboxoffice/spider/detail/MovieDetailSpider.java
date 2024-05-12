package com.example.movieboxoffice.spider.detail;

import com.example.movieboxoffice.entity.MovieDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Component
public class MovieDetailSpider {
    @Autowired
    private MovieDetailBaiduPageProcessor spiderPageProcessor;
    @Autowired
    private MovieDetailBaiduPipeline pipeline;


    private static String url = "https://baike.baidu.com/item/";


    public  Spider getDefaultSpider(MovieDo movieDo){
        return Spider.create(spiderPageProcessor)
                .addPipeline(pipeline)
                .addUrl(url+movieDo.getMovieName())
                .setUUID(movieDo.getMovieCode().toString())
                .thread(3);
    }





}
