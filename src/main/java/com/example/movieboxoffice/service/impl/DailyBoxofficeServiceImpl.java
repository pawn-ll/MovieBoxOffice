package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.DailyBoxoffice;
import com.example.movieboxoffice.entity.vo.DailyBoxofficeVO;
import com.example.movieboxoffice.mapper.DailyBoxofficeMapper;
import com.example.movieboxoffice.service.IDailyBoxofficeService;
import com.example.movieboxoffice.spider.daily.DailyBoxOfficeSpider;
import com.example.movieboxoffice.utils.MyDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-07
 */
@Service
public class DailyBoxofficeServiceImpl extends ServiceImpl<DailyBoxofficeMapper, DailyBoxoffice> implements IDailyBoxofficeService {

    @Autowired
    private DailyBoxOfficeSpider dailyBoxOfficeSpider;
    @Autowired
    private DailySumBoxofficeServiceImpl dailySumBoxofficeService;

    @Override
    public void todaySpiderCrawl() {
        /*
        引入redis用缓存5秒更新一次
        更新数据库1小时一次
         */
        String date = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);
        deleteByDates(date,date);
        dailySumBoxofficeService.deleteByDates(date,date);
        dailyBoxOfficeSpider.getDefaultSpider().run();
    }

    @Override
    public void spiderCrawl(String date){
        dailyBoxOfficeSpider.getDateSpider(date).run();
    }

    @Override
    public void deleteByDates(String startDate, String endDate) {
        LambdaQueryWrapper<DailyBoxoffice> wrapper = new LambdaQueryWrapper<DailyBoxoffice>()
                .ge(DailyBoxoffice::getRecordDate, startDate)
                .le(DailyBoxoffice::getRecordDate, endDate);
        this.baseMapper.delete(wrapper);
    }

    @Override
    public List<DailyBoxofficeVO> today() {
        List<DailyBoxoffice> dailyBoxoffices = this.baseMapper.selectList(new LambdaQueryWrapper<DailyBoxoffice>()
                .eq(DailyBoxoffice::getRecordDate, MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD)));
        List<DailyBoxofficeVO> list = new ArrayList<>();
        if (dailyBoxoffices.size() > 0){
            for (DailyBoxoffice dailyBoxoffice : dailyBoxoffices) {
                DailyBoxofficeVO dailyBoxofficeVO = new DailyBoxofficeVO();
                BeanUtils.copyProperties(dailyBoxoffice,dailyBoxofficeVO);
                list.add(dailyBoxofficeVO);
            }
        }
        return list;
    }
}
