package com.example.movieboxoffice.spider.detail;


import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 *
 * 电影详情爬虫页面
 * @author walnut
 * @since  2024.5.10
 *
 */
@Component
public class MovieDetailPageProcessor implements PageProcessor {

    private Site site = Site.me().setCharset("utf-8").setRetryTimes(3)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");


    @Override
    public void process(Page page) {

        Html html = page.getHtml();
        List<String> lista = html.xpath("//div[@class='basicInfo_spa7J J-basic-info']//dt[@class='basicInfoItem_s_YWZ itemName_Un9Kz']/text()").all();
        List<String> listb = html.xpath("//div[@class='basicInfo_spa7J J-basic-info']//dd[@class='basicInfoItem_s_YWZ itemValue_sOz6C']").all();
        for (int i = 0; i < lista.size(); i++){
            String s = lista.get(i);
            char first = s.charAt(0);
            char last = s.charAt(s.length()-1);
            if (first == '导' && last =='演'){
                page.putField("director",getResult(listb.get(i)));
                continue;
            }
            if (first == '主' && last =='演'){
                page.putField("actor",getResult(listb.get(i)));
                continue;
            }
            if (first == '类' && last =='型'){
                page.putField("type",getResult(listb.get(i)));
                continue;
            }
            if (first == '片' && last =='长'){
                page.putField("length",getResult(listb.get(i)));
                continue;
            }
            if (s.equals("制片国家") || s.equals("制片地区")){
                page.putField("area",getResult(listb.get(i)));
            }
        }
        List<String> instruction = html.xpath("//div[@class='J-lemma-content']/div[2]/span/text()").all();
        if (instruction.size()>0){
            StringBuilder sb = new StringBuilder();
            for (String str : instruction){
                sb.append(str);
            }
            page.putField("instruction",sb.toString());
        }
        String pic = html.xpath("//div[@class='albumListBox_dODAM']/div[@class='albumContent_FaHVd']/div[2]/img").get();
        if (pic!=null){
            int start = pic.indexOf("http");
            int end = pic.indexOf("?");
            pic = pic.substring(start ,end);
            page.putField("poster",pic);
        }

//        page.putField("list",(jsonObject.get("list")).toString());
//        page.putField("nationalSales",(jsonObject.get("nationalSales")));
//        String url = page.getRequest().getUrl();
//        int index = url.indexOf("date=");
//        String date = url.substring(index +5, index + 15);
//        page.putField("date",date);

    }

    @Override
    public Site getSite() {
        return site;
    }

    private String getResult(String html){
        List<String> list = new Html(html).xpath("//span[@class='text_jaYku']/text()").all();
        if (list.size()==0){
            list = new Html(html).xpath("//span[@class='text_jaYku']/a/text()").all();
        }
        if (list.size()==0){
            return "";
        }else if (list.size()==1){
            return list.get(0);
        }else {
            StringBuilder sb = new StringBuilder();
            for (String s : list){
                sb.append(s).append(" ");
            }
            return sb.toString();
        }


    }
}
