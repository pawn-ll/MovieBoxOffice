package com.example.movieboxoffice.service;

import com.example.movieboxoffice.entity.SiteVisitorCount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-06-18
 */
public interface ISiteVisitorCountService extends IService<SiteVisitorCount> {

    SiteVisitorCount getSiteVisitorCount();

}
