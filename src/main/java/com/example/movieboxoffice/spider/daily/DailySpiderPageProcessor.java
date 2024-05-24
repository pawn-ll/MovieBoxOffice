package com.example.movieboxoffice.spider.daily;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 *
 * 日常票房爬虫页面
 * @author walnut
 * @since  2024.5.7
 *
 */
@Component
public class DailySpiderPageProcessor implements PageProcessor {

    private Site site = Site.me().setCharset("utf-8").setRetryTimes(3)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");


    @Override
    public void process(Page page) {

        JSONObject jsonObject = JSON.parseObject(page.getRawText());
        page.putField("list",(jsonObject.get("list")).toString());
        page.putField("nationalSales",(jsonObject.get("nationalSales")));
        String url = page.getRequest().getUrl();
        int index = url.indexOf("date=");
        String date = url.substring(index +5, index + 15);
        page.putField("date",date);


    }

    @Override
    public Site getSite() {
        return site;
    }
}
