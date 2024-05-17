package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.DailySumBoxoffice;
import com.example.movieboxoffice.entity.vo.DailySumBoxofficeVO;
import com.example.movieboxoffice.entity.vo.HistoygramVO;
import com.example.movieboxoffice.mapper.DailySumBoxofficeMapper;
import com.example.movieboxoffice.service.IDailySumBoxofficeService;
import com.example.movieboxoffice.utils.MyDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-07
 */
@Service
public class DailySumBoxofficeServiceImpl extends ServiceImpl<DailySumBoxofficeMapper, DailySumBoxoffice> implements IDailySumBoxofficeService {

    @Override
    public void deleteByDates(String startDate, String endDate) {
        LambdaQueryWrapper<DailySumBoxoffice> wrapper = new LambdaQueryWrapper<DailySumBoxoffice>()
                .ge(DailySumBoxoffice::getDate, startDate)
                .le(DailySumBoxoffice::getDate, endDate);
        this.baseMapper.delete(wrapper);

    }

    @Override
    public DailySumBoxofficeVO today() {
        DailySumBoxoffice dailySumBoxoffice = new DailySumBoxoffice();
        dailySumBoxoffice.setDate(MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD));
        dailySumBoxoffice = this.baseMapper.selectOne(new QueryWrapper<DailySumBoxoffice>(dailySumBoxoffice));
        DailySumBoxofficeVO sumBoxofficeVO = new DailySumBoxofficeVO();
        BeanUtils.copyProperties(dailySumBoxoffice, sumBoxofficeVO);
        return sumBoxofficeVO;
    }

    @Override
    public HistoygramVO getDatesHistoygram(String startDate, String endDate) {
        List<DailySumBoxoffice> dailySumBoxoffices = this.baseMapper.selectList(new LambdaQueryWrapper<DailySumBoxoffice>()
                .ge(DailySumBoxoffice::getDate, startDate)
                .le(DailySumBoxoffice::getDate, endDate)
                .orderByAsc(DailySumBoxoffice::getDate));
        HistoygramVO histoygramVO = new HistoygramVO();
        if (dailySumBoxoffices.size() > 0) {
            histoygramVO.setXAxis(dailySumBoxoffices.stream().map(DailySumBoxoffice::getDate).collect(Collectors.toList()));
            List<String> yAxis = new ArrayList<>();
            dailySumBoxoffices.forEach(dailySumBoxoffice -> {
                String sumBoxoffice = dailySumBoxoffice.getSumBoxoffice();
                yAxis.add((sumBoxoffice.substring(0,sumBoxoffice.length()-2)));
            });
            histoygramVO.setYAxis(yAxis);

        }
        return histoygramVO;
    }
}
