package com.example.movieboxoffice.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movieboxoffice.entity.DailyBoxoffice;
import com.example.movieboxoffice.entity.DailySumBoxoffice;
import com.example.movieboxoffice.entity.MovieDo;
import com.example.movieboxoffice.entity.SecondDo;
import com.example.movieboxoffice.service.RedisService;
import com.example.movieboxoffice.service.impl.*;
import com.example.movieboxoffice.spider.daily.DailyBoxOfficeSpider;
import com.example.movieboxoffice.spider.detail.MovieDetailDoubanService;
import com.example.movieboxoffice.utils.MyConstant;
import com.example.movieboxoffice.utils.MyDateUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Log4j2
public class SpiderTask {

    /**
     * 1、每日数据
     * 2、详情爬取
     * 3、海报base64图片爬取
     *
     */

    @Autowired
    private DailyBoxOfficeSpider dailyBoxOfficeSpider;
    @Autowired
    private DailySumBoxofficeServiceImpl dailySumBoxofficeService;
    @Autowired
    private DailyBoxofficeServiceImpl dailyBoxofficeService;
    @Autowired
    private SecondDoServiceImpl secondDoService;
    @Autowired
    private MovieDoServiceImpl movieDoService;
    @Autowired
    private MovieDetailDoubanService doubanService;
    @Autowired
    private MovieDetailServiceImpl movieDetailService;
    @Autowired
    private RedisService redisService;

    public void todaySpiderCrawl() {
        /*
        引入redis用缓存60秒更新一次
         */
        dailyBoxOfficeSpider.getDefaultSpider().run();
    }

//    更新数据库1小时一次
    @Transactional(rollbackFor =Exception.class)
    public void saveDailyData() {
        String date = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);
        List<DailyBoxoffice> dailyBoxoffices = JSONArray.parseArray(redisService.get(MyConstant.TODAY_DAILY_BOXOFFICELIST), DailyBoxoffice.class);
        if (dailyBoxoffices.size() > 0){
            dailyBoxofficeService.deleteByDates(date,date);
            for (DailyBoxoffice dailyBoxoffice : dailyBoxoffices) {
                dailyBoxofficeService.save(dailyBoxoffice);
            }
        }
        DailySumBoxoffice dailySumBoxoffice = JSONObject.parseObject(redisService.get(MyConstant.TODAY_DAILY_SUMBOXOFFICE), DailySumBoxoffice.class);
        if (dailySumBoxoffice != null){
            dailySumBoxofficeService.deleteByDates(date,date);
            dailySumBoxofficeService.save(dailySumBoxoffice);
        }
    }


    public void spiderCrawl(String date){
        dailyBoxOfficeSpider.getDateSpider(date).run();
    }

    /**
     * 详细爬虫抓取功能
     * 该方法从数据库中选取未处理的电影信息，然后通过豆瓣服务获取每部电影的详细信息。
     * 为了防止请求过于频繁，每次请求后会设置一定的休息时间。
     *
     * @throws InterruptedException 如果线程在睡眠时被中断，则抛出此异常
     */
    public void detailSpiderCrawl() throws InterruptedException {
        List<MovieDo> movieDos = movieDoService.selectNotDO();

        for (MovieDo movieDo : movieDos){
            doubanService.getMovieDetail(movieDo.getMovieName(),movieDo.getMovieCode(),false);
            System.out.println("--------------休息-----------------");
            Thread.sleep(1000*6);
        }
    }

    /**
     * 实施针对未处理电影详情的第二次爬取操作。
     * 该方法会从secondDoService获取一个未处理电影列表，然后遍历这个列表，
     * 对每个电影调用doubanService来获取电影的详细信息。每次爬取之间会有一个固定的休息时间。
     *
     * @throws InterruptedException 如果线程在睡眠时被中断，则会抛出此异常。
     */
    public void detailSpiderSecondCrawl() throws InterruptedException {

        List<SecondDo> notDOList = secondDoService.getNotDOList();

        for (SecondDo movieDo : notDOList){
            doubanService.getMovieDetail(movieDo.getMovieName(),movieDo.getMovieCode(),true);
            System.out.println("--------------休息-----------------");
            Thread.sleep(1000*6);
        }

    }

    public void setPosterBase64() {
        movieDetailService.setPosterBase64();
    }

    /**
     * 重新抓取指定年月的数据。
     * 对于给定的年份和月份，该方法首先会清除原有该时间段内的数据，然后重新进行抓取。
     *
     * @param year 年份，整型。
     * @param month 月份，整型。注意，月份从1开始，而非0开始。
     */
    public void reCrawlMonth(Integer year, Integer month){
        List<String> list = MyDateUtils.generateDatesOfYearMonth(year, month);
        String startDate = list.get(0);
        String endDate = list.get(list.size()-1);

        dailyBoxofficeService.deleteByDates(startDate, endDate);
        dailySumBoxofficeService.deleteByDates(startDate, endDate);
        crawlMonth(year, month);
    }

    private void crawlMonth(Integer year, Integer month){
        long startTime = System.currentTimeMillis();
        List<String> list = MyDateUtils.generateDatesOfYearMonth(year, month);
        for (String date : list) {
            log.error("开始爬取!" + date + "-----------------------------------------------");
            spiderCrawl(date);
        }
        long endTime =  System.currentTimeMillis();
        long duration = (endTime - startTime)/1000;
        log.error("爬取 "+year+"-"+month+" 完成! 耗时: " + duration + " 秒");
    }


}
