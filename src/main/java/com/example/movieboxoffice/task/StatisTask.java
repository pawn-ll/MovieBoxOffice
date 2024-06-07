package com.example.movieboxoffice.task;

import com.example.movieboxoffice.service.impl.DailyBoxofficeServiceImpl;
import com.example.movieboxoffice.utils.MyDateUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class StatisTask {

    @Autowired
    DailyBoxofficeServiceImpl dailyBoxofficeService;


    @Scheduled(cron = "35 30 0 * * ?")
    public void statisDailyBoxoffice() {
        log.info("开始统计今年票房");
        dailyBoxofficeService.statisDaily(1);
        dailyBoxofficeService.statisDaily(2);
        log.info("今年票房统计完成");
    }

    public void statisHistory(Integer year) {
        long startTime = System.currentTimeMillis();
        String startDate = year.toString()+"-01-01";
        String endDate = year.toString()+"-12-31";
        dailyBoxofficeService.statisHistory(2,startDate,endDate);
        for (int i = 1; i <= 12; i++) {
            log.info("开始统计"+year+"年"+i+"月");
            List<String> list = MyDateUtils.generateDatesOfYearMonth(year, i);
            dailyBoxofficeService.statisHistory(1,list.get(0),list.get(list.size()-1));
        }
        long endTime =  System.currentTimeMillis();
        long duration = (endTime - startTime)/1000;
        log.error(" 耗时: " + duration + " 秒");
    }
}
