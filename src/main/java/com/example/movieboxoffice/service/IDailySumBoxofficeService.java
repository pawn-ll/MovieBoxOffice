package com.example.movieboxoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.DailySumBoxoffice;
import com.example.movieboxoffice.entity.vo.DailySumBoxofficeVO;
import com.example.movieboxoffice.entity.vo.HistoygramVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-07
 */
public interface IDailySumBoxofficeService extends IService<DailySumBoxoffice> {

    void deleteByDates(String startDate , String endDate);

    DailySumBoxofficeVO today() ;

    HistoygramVO getDatesHistoygram(String startDate , String endDate);
}
