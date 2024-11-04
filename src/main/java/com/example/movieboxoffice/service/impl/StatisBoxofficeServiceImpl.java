package com.example.movieboxoffice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.StatisBoxoffice;
import com.example.movieboxoffice.mapper.StatisBoxofficeMapper;
import com.example.movieboxoffice.service.IStatisBoxofficeService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 服务实现类
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
                .eq(StatisBoxoffice::getMovieCode, movieCode)
                .eq(StatisBoxoffice::getStatisType, statisType)
                .eq(StatisBoxoffice::getStatisInterval, statisInterval));
    }

    @Override
    public List<StatisBoxoffice> getStatisList(Integer statisType, String statisInterval) {
        return baseMapper.selectList(new LambdaQueryWrapper<StatisBoxoffice>()
                .eq(StatisBoxoffice::getStatisType, statisType)
                .eq(StatisBoxoffice::getStatisInterval, statisInterval)
                .gt(StatisBoxoffice::getStatisSumBoxoffice, 1)
                .orderByDesc(StatisBoxoffice::getStatisSumBoxoffice));
    }

    @Override
    public void outputStatis(List<StatisBoxoffice> list , HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(),StatisBoxoffice.class)
                .sheet("统计数据")
                .doWrite(list);
    }

}
