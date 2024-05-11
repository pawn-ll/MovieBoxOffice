package com.example.movieboxoffice;

import com.example.movieboxoffice.service.impl.DailyBoxofficeServiceImpl;
import com.example.movieboxoffice.service.impl.DailySumBoxofficeServiceImpl;
import com.example.movieboxoffice.service.impl.MovieDetailServiceImpl;
import com.example.movieboxoffice.utils.MyDateUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest()
@Log4j2
class MovieBoxOfficeApplicationTests {

    @Autowired
    private DailyBoxofficeServiceImpl dailyBoxofficeService;
    @Autowired
    private DailySumBoxofficeServiceImpl dailySumBoxofficeService;
    @Autowired
    private MovieDetailServiceImpl movieDetailService;

    @Test
    void contextLoads() {
    }


    @Test
    public void detailSpider(){
        movieDetailService.crawlDetail();
    }



    @Test
    public void dateSpider(){
        dailyBoxofficeService.todaySpiderCrawl();
//        dailyBoxofficeService.spiderCrawl("2024-05-10");

    }

    @Test
    public void reCrawl(){
        reCrawlMonth(2022, 9);
    }

    private void reCrawlMonth(Integer year, Integer month){
        List<String> list = MyDateUtils.generateDatesOfYearMonth(year, month);
        String startDate = list.get(0);
        String endDate = list.get(list.size()-1);

        dailyBoxofficeService.deleteByDates(startDate, endDate);
        dailySumBoxofficeService.deleteByDates(startDate, endDate);
        crawlMonth(year, month);
    }

    private void crawlMonth(Integer year, Integer month){
        long startTime = System.nanoTime();
        List<String> list = MyDateUtils.generateDatesOfYearMonth(year, month);
        for (String date : list) {
            log.error("开始爬取!" + date + "-----------------------------------------------");
            dailyBoxofficeService.spiderCrawl(date);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000/1000/1000;
        log.error("爬取 "+year+"-"+month+" 完成! 耗时: " + duration + " 秒");
    }







}
