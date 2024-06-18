package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.movieboxoffice.entity.StatisBoxoffice;
import com.example.movieboxoffice.mapper.StatisBoxofficeMapper;
import com.example.movieboxoffice.service.IStatisBoxofficeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-06-05
 */
@Service
public class StatisBoxofficeServiceImpl extends ServiceImpl<StatisBoxofficeMapper, StatisBoxoffice> implements IStatisBoxofficeService {


    @Override
    public StatisBoxoffice getStatisBoxoffice(Long movieCode, Integer statisType, String statisInterval) {
        return baseMapper.selectOne(new LambdaQueryWrapper<StatisBoxoffice>()
                .eq(StatisBoxoffice::getMovieCode,movieCode)
                .eq(StatisBoxoffice::getStatisType,statisType)
                .eq(StatisBoxoffice::getStatisInterval,statisInterval));
    }

    @Override
    public List<StatisBoxoffice> getStatisList(Integer statisType, String statisInterval) {
        return baseMapper.selectList(new LambdaQueryWrapper<StatisBoxoffice>()
                .eq(StatisBoxoffice::getStatisType,statisType)
                .eq(StatisBoxoffice::getStatisInterval,statisInterval)
                .gt(StatisBoxoffice::getStatisSumBoxoffice,1)
                .orderByDesc(StatisBoxoffice::getStatisSumBoxoffice)) ;
    }

}
