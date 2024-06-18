package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.SiteVisitorDayCount;
import com.example.movieboxoffice.mapper.SiteVisitorDayCountMapper;
import com.example.movieboxoffice.service.ISiteVisitorDayCountService;
import com.example.movieboxoffice.utils.MyDateUtils;
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
public class SiteVisitorDayCountServiceImpl extends ServiceImpl<SiteVisitorDayCountMapper, SiteVisitorDayCount> implements ISiteVisitorDayCountService {

    @Override
    public SiteVisitorDayCount getTodayCount() {
        String today = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);
        return baseMapper.selectOne(new LambdaQueryWrapper<SiteVisitorDayCount>()
                .eq(SiteVisitorDayCount::getVisitDate, today));
    }
}
