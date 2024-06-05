package com.example.movieboxoffice.service;

import com.example.movieboxoffice.entity.StatisBoxoffice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-06-05
 */
public interface IStatisBoxofficeService extends IService<StatisBoxoffice> {

    StatisBoxoffice getStatisBoxoffice(Long movieCode, Integer statisType , String statisInterval);

}
