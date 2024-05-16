package com.example.movieboxoffice.service;

import com.example.movieboxoffice.entity.DailyBoxoffice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.vo.DailyBoxofficeVO;
import com.example.movieboxoffice.entity.vo.HistoygramVO;

import java.util.List;

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

    List<DailyBoxofficeVO> today();

    HistoygramVO getDatesHistoygram(String startDate, String endDate, Long movieCode);
}
