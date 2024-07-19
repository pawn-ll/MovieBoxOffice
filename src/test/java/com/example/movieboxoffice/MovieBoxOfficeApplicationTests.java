package com.example.movieboxoffice;

import com.example.movieboxoffice.service.RedisService;
import com.example.movieboxoffice.service.impl.*;
import com.example.movieboxoffice.spider.HistoryBoxOfficeSpider;
import com.example.movieboxoffice.spider.detail.ActorDetailSpider;
import com.example.movieboxoffice.task.ActorTask;
import com.example.movieboxoffice.task.SpiderTask;
import com.example.movieboxoffice.task.StatisTask;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
@Log4j2
class MovieBoxOfficeApplicationTests {

    @Autowired
    private SpiderTask spiderTask;
    @Autowired
    private StatisTask statisTask;
    @Autowired
    private RedisService redisService;
    @Autowired
    private StatisBoxofficeServiceImpl statisBoxofficeService;
    @Autowired
    private HistoryBoxOfficeSpider historySpider;
    @Autowired
    private MovieBoxofficeServiceImpl movieBoxofficeService;
    @Autowired
    private MovieDetailServiceImpl movieDetailService;
    @Autowired
    private MoviePosterServiceImpl moviePosterService;
    @Autowired
    private ActorServiceImpl actorService;
    @Autowired
    private ActorDetailSpider actorDetailSpider;
    @Autowired
    private ActorTask actorTask;

    @Test
    public void testService() throws InterruptedException {
//        spiderTask.updateSumBoxoffice();
        spiderTask.getDetailByUrl();
//        spiderTask.setPosterBase64();
//        actorService.historyInsert();
//        actorTask.actorDetailSpider();

        System.out.println();
    }


    @Test
    public void secondDetailSpider() throws InterruptedException {
        spiderTask.detailSpiderSecondCrawl();

    }
    @Test
    public void detailSpider() throws InterruptedException {
        spiderTask.detailSpiderCrawl();
    }


    /**
     * 运行日期爬虫任务的测试方法。
     * 该方法没有参数，也没有返回值。
     * 主要执行以下操作：
     * 1. 记录开始时间；
     * 2. 执行当天的爬虫抓取任务；
     * 3. 记录结束时间；
     * 4. 计算并打印任务执行时间。
     */
    @Test
    public void dateSpider(){
        long startTime = System.currentTimeMillis();
        spiderTask.todaySpiderCrawl();
//        spiderTask.saveDailyData();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("耗时"+duration + "ms" );
    }

    @Test
    public void reCrawl(){
        spiderTask.reCrawlDate("2024-07-17");
//        spiderTask.reCrawlMonth(2024, 5);
    }








}
