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
//        List<BoxOfficeWeb> list = JSON.parseArray((jsonObject.get("list")).toString(), BoxOfficeWeb.class);
//        DailyBoxoffice dailyBoxoffice = new DailyBoxoffice();
//        dailyBoxoffice.setRecordDate(MyDateUtils.parseDate(date,MyDateUtils.YYMMDD));
//        for (BoxOfficeWeb boxOffice :list ){
//            dailyBoxoffice.setMovieName(boxOffice.getName());
//            dailyBoxoffice.setSumBoxoffice(boxOffice.getSumSalesDesc());
//            dailyBoxoffice.setSumSplitBoxoffice(boxOffice.getSumSplitSalesDesc());
//            dailyBoxoffice.setReleaseDays(Integer.parseInt(boxOffice.getReleaseDays()));
//            dailyBoxoffice.setDayBoxoffice(new BigDecimal(boxOffice.getSalesInWanDesc()));
//            dailyBoxoffice.setDaySplitBoxoffice(new BigDecimal(boxOffice.getSplitSalesInWanDesc()));
//            dailyBoxoffice.setDayBoxofficeRate(boxOffice.getSalesRateDesc());
//            dailyBoxoffice.setDaySplitBoxofficeRate(boxOffice.getSplitSalesRateDesc());
//            dailyBoxoffice.setDayArrangeRate(boxOffice.getSessionRateDesc());
//            dailyBoxoffice.setDaySeatRate(boxOffice.getSeatRateDesc());
//
//            dailyBoxofficeMapper.insert(dailyBoxoffice);
//        }

//        JSONObject nationalSales = (JSONObject)jsonObject.get("nationalSales");
//        JSONObject salesDesc = (JSONObject)nationalSales.get("salesDesc");
//        JSONObject splitSalesDesc = (JSONObject)nationalSales.get("splitSalesDesc");
//        String sales = salesDesc.get("value").toString()+salesDesc.get("unit").toString();
//        String splitSales = splitSalesDesc.get("value").toString()+splitSalesDesc.get("unit").toString();
//        DailySumBoxoffice dailySumBoxoffice = new DailySumBoxoffice();
//        dailySumBoxoffice.setSumBoxoffice(sales);
//        dailySumBoxoffice.setSumSplitBoxoffice(splitSales);
//        dailySumBoxoffice.setDate(MyDateUtils.parseDate(date,MyDateUtils.YYMMDD));
//        dailySumBoxofficeMapper.insert(dailySumBoxoffice);


    }

    @Override
    public Site getSite() {
        return site;
    }
}
