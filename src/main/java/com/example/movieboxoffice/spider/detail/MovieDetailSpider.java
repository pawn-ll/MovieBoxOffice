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


    private static String baiduUrl = "https://baike.baidu.com/item/";

    private static String doubanUrl = "https://movie.douban.com/j/subject_suggest?q=";



    public  Spider getBaiduSpider(MovieDo movieDo){
        return Spider.create(spiderPageProcessor)
                .addPipeline(pipeline)
                .addUrl(baiduUrl+movieDo.getMovieName())
                .setUUID(movieDo.getMovieCode().toString())
                .thread(3);
    }





}
