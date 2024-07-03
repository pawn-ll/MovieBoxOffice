package com.example.movieboxoffice.task;


import com.example.movieboxoffice.spider.detail.ActorDetailSpider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ActorTask {

    @Autowired
    private ActorDetailSpider actorDetailSpider;

    @Scheduled(cron = "30 30 * * * ?")
    public void actorDetailSpider() throws InterruptedException {
        log.info("---------演员详情爬取------------");
        actorDetailSpider.actorDetailSpider();
    }
}
