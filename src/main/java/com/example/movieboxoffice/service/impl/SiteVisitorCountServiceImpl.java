package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.movieboxoffice.entity.SiteVisitorCount;
import com.example.movieboxoffice.mapper.SiteVisitorCountMapper;
import com.example.movieboxoffice.service.ISiteVisitorCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-06-18
 */
@Service
public class SiteVisitorCountServiceImpl extends ServiceImpl<SiteVisitorCountMapper, SiteVisitorCount> implements ISiteVisitorCountService {

    @Override
    public SiteVisitorCount getSiteVisitorCount() {
        return baseMapper.selectOne(new LambdaQueryWrapper<SiteVisitorCount>()
                .eq(SiteVisitorCount::getSiteName, "boxoffice"));
    }
}
