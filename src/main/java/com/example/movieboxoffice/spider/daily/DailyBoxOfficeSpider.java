package com.example.movieboxoffice.spider.daily;

import com.example.movieboxoffice.utils.MyDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Component
public class DailyBoxOfficeSpider {

    @Autowired
    private DailySpiderPageProcessor spiderPageProcessor;
    @Autowired
    private DailyBoxOfficePipeline boxOfficePipeline;


    private static String date = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);

    private static String url = "https://zgdypf.zgdypw.cn/getDayData?date="+date+"&withSvcFee=true";


    public  Spider getDefaultSpider(){
        return Spider.create(spiderPageProcessor).addPipeline(boxOfficePipeline).addUrl(url).thread(3);
    }

    public Spider getDateSpider(String dates){

        String urls = "https://zgdypf.zgdypw.cn/getDayData?withSvcFee=true&date="+dates+"&dateType=0";
        return Spider.create(spiderPageProcessor).addPipeline(boxOfficePipeline).addUrl(urls).thread(3);
    }




}
