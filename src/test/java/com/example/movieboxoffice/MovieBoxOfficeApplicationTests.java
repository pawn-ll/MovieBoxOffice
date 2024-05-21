package com.example.movieboxoffice;

import com.example.movieboxoffice.entity.MovieDo;
import com.example.movieboxoffice.entity.SecondDo;
import com.example.movieboxoffice.service.impl.*;
import com.example.movieboxoffice.spider.detail.MovieDetailDoubanService;
import com.example.movieboxoffice.utils.MyDateUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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
    @Autowired
    private MovieDoServiceImpl movieDoService;
    @Autowired
    private MovieDetailDoubanService doubanService;
    @Autowired
    private  MovieBoxofficeServiceImpl movieBoxofficeService;
    @Autowired
    private SecondDoServiceImpl secondDoService;

    @Test
    public void testService() {
        movieBoxofficeService.insertAll();
//        movieDoService.verifyMovieCode();
        System.out.println();
    }


    @Test
    public void detailListSpider() throws InterruptedException {
        List<SecondDo> notDOList = secondDoService.getNotDOList();
//        List<MovieDo> movieDos = movieDoService.selectNotDO();
        List<MovieDo> movieDos =new ArrayList<>();
        for (SecondDo secondDo : notDOList){
            MovieDo movieDo = new MovieDo();
            movieDo.setMovieCode(secondDo.getMovieCode());
            movieDo.setMovieName(secondDo.getMovieName());
            movieDos.add(movieDo);
        }
        for (MovieDo movieDo : movieDos){
            doubanService.getMovieDetail(movieDo);
            System.out.println("--------------休息-----------------");
            Thread.sleep(1000*6);
        }

    }
    @Test
    public void todayDetailSpider() throws InterruptedException {
        List<MovieDo> movieDos = movieDoService.selectTodayNotDO();
        if (movieDos.size() == 0){
            return;
        }
        for (MovieDo movieDo : movieDos){
            doubanService.getMovieDetail(movieDo);
            System.out.println("--------------休息-----------------");
            Thread.sleep(1000*6);
        }

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
