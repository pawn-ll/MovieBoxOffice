package com.example.movieboxoffice.service;

import com.example.movieboxoffice.entity.DailyBoxoffice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-07
 */
public interface IDailyBoxofficeService extends IService<DailyBoxoffice> {

    void todaySpiderCrawl();

    void spiderCrawl(String date);

    void deleteByDates(String startDate , String endDate);

}
