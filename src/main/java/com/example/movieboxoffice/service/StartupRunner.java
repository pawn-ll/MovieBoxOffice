package com.example.movieboxoffice.service;

import com.example.movieboxoffice.entity.Ip;
import com.example.movieboxoffice.entity.SiteVisitorCount;
import com.example.movieboxoffice.entity.SiteVisitorDayCount;
import com.example.movieboxoffice.service.impl.IpServiceImpl;
import com.example.movieboxoffice.service.impl.SiteVisitorCountServiceImpl;
import com.example.movieboxoffice.service.impl.SiteVisitorDayCountServiceImpl;
import com.example.movieboxoffice.utils.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class StartupRunner {

    @Autowired
    private RedisService redisService;
    @Autowired
    private IpServiceImpl ipService;
    @Autowired
    private SiteVisitorCountServiceImpl siteVisitorCountService;
    @Autowired
    private SiteVisitorDayCountServiceImpl siteVisitorDayCountService;

    @PostConstruct
    public void init(){
        List<Ip> blackIpList = ipService.getBlackIpList();
        if (blackIpList != null && blackIpList.size() > 0) {
            for (Ip ip : blackIpList) {
                redisService.sAdd(MyConstant.BLACK_IP_LIST, ip.getIp());
            }
        }
        List<Ip> whiteIpList = ipService.getWhiteIpList();
        if (whiteIpList != null && whiteIpList.size() > 0) {
            for (Ip ip : whiteIpList) {
                redisService.sAdd(MyConstant.WHITE_IP_LIST, ip.getIp());
            }
        }
        Object count = redisService.get(MyConstant.WEB_SITE_VISITOR_COUNT);
        if (count == null) {
            SiteVisitorCount siteVisitorCount = siteVisitorCountService.getSiteVisitorCount();
            redisService.set(MyConstant.WEB_SITE_VISITOR_COUNT, siteVisitorCount.getSiteVisitorCount());
        }
        Object dayCount = redisService.get(MyConstant.WEB_SITE_VISITOR_COUNT_TODAY);
        if (dayCount == null) {
            SiteVisitorDayCount siteVisitorDayCount = siteVisitorDayCountService.getTodayCount();
            if (siteVisitorDayCount != null) {
                redisService.set(MyConstant.WEB_SITE_VISITOR_COUNT_TODAY, siteVisitorDayCount.getSiteVisitorCount());
            }else{
                redisService.set(MyConstant.WEB_SITE_VISITOR_COUNT_TODAY, 0);
            }
        }
    }
}
