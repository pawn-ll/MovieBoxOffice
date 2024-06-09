package com.example.movieboxoffice.service;

import com.example.movieboxoffice.entity.DailyBoxoffice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.StatisBoxoffice;
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


    void deleteByDates(String startDate , String endDate);

    List<DailyBoxofficeVO> today();

    HistoygramVO getDatesHistoygram(String startDate, String endDate, Long movieCode);

    HistoygramVO getWeekHistoygram(Long movieCode);

    DailyBoxoffice latestBoxoffice(Long movieCode);

    List<StatisBoxoffice> getDatesList(String startDate, String endDate);

    List<Long> getMoviesCodeList();

    void verifyCode(Long oldCode,Long newCode);

    void statisDaily(Integer statisType);

    void statisHistory(Integer statisType, String startDate, String endDate);

    List<StatisBoxoffice> getStatis(String startDate, String endDate);

    List<DailyBoxofficeVO> day(String date);
}
