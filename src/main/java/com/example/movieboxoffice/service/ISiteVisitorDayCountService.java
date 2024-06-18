package com.example.movieboxoffice.service;

import com.example.movieboxoffice.entity.SiteVisitorDayCount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-06-18
 */
public interface ISiteVisitorDayCountService extends IService<SiteVisitorDayCount> {

    SiteVisitorDayCount getTodayCount();

}
