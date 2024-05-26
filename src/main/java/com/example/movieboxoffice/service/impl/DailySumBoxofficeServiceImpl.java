package com.example.movieboxoffice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.DailySumBoxoffice;
import com.example.movieboxoffice.entity.vo.DailySumBoxofficeVO;
import com.example.movieboxoffice.entity.vo.HistoygramVO;
import com.example.movieboxoffice.mapper.DailySumBoxofficeMapper;
import com.example.movieboxoffice.service.IDailySumBoxofficeService;
import com.example.movieboxoffice.service.RedisService;
import com.example.movieboxoffice.utils.MyConstant;
import com.example.movieboxoffice.utils.MyDateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedisService redisService;

    @Override
    public void deleteByDates(String startDate, String endDate) {
        LambdaQueryWrapper<DailySumBoxoffice> wrapper = new LambdaQueryWrapper<DailySumBoxoffice>()
                .ge(DailySumBoxoffice::getDate, startDate)
                .le(DailySumBoxoffice::getDate, endDate);
        this.baseMapper.delete(wrapper);

    }

    @Override
    public DailySumBoxofficeVO today() {
        DailySumBoxoffice dailySumBoxoffice = null;
        String s = (String)redisService.get(MyConstant.TODAY_DAILY_SUMBOXOFFICE);
        if (!StringUtils.isEmpty(s)){
            dailySumBoxoffice = JSONObject.parseObject(s, DailySumBoxoffice.class);
        }else {
            String date = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);
            dailySumBoxoffice = this.baseMapper.selectOne(new LambdaQueryWrapper<DailySumBoxoffice>()
                    .eq(DailySumBoxoffice::getDate, date));
            redisService.set(MyConstant.TODAY_DAILY_SUMBOXOFFICE, JSONObject.toJSONString(dailySumBoxoffice));
        }
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
