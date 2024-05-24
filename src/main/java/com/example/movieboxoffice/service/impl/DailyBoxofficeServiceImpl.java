package com.example.movieboxoffice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.ConditionException;
import com.example.movieboxoffice.entity.DailyBoxoffice;
import com.example.movieboxoffice.entity.vo.DailyBoxofficeVO;
import com.example.movieboxoffice.entity.vo.HistoygramVO;
import com.example.movieboxoffice.mapper.DailyBoxofficeMapper;
import com.example.movieboxoffice.service.IDailyBoxofficeService;
import com.example.movieboxoffice.service.RedisService;
import com.example.movieboxoffice.utils.MyConstant;
import com.example.movieboxoffice.utils.MyDateUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class DailyBoxofficeServiceImpl extends ServiceImpl<DailyBoxofficeMapper, DailyBoxoffice> implements IDailyBoxofficeService {


    @Autowired
    private RedisService redisService;



    @Override
    public void deleteByDates(String startDate, String endDate) {
        LambdaQueryWrapper<DailyBoxoffice> wrapper = new LambdaQueryWrapper<DailyBoxoffice>()
                .ge(DailyBoxoffice::getRecordDate, startDate)
                .le(DailyBoxoffice::getRecordDate, endDate);
        this.baseMapper.delete(wrapper);
    }

    @Override
    public List<DailyBoxofficeVO> today() {
        List<DailyBoxoffice> dailyBoxoffices = null;
        String s = redisService.get(MyConstant.TODAY_DAILY_BOXOFFICELIST);
        if (StringUtils.isNotEmpty(s)){
            dailyBoxoffices = JSONArray.parseArray(s, DailyBoxoffice.class);
        }else {
            dailyBoxoffices = this.baseMapper.selectList(new LambdaQueryWrapper<DailyBoxoffice>()
                .eq(DailyBoxoffice::getRecordDate, MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD)));
            redisService.set(MyConstant.TODAY_DAILY_BOXOFFICELIST,JSONArray.toJSONString(dailyBoxoffices));
        }
        List<DailyBoxofficeVO> list = new ArrayList<>();
        if (dailyBoxoffices.size() > 0){
            for (DailyBoxoffice dailyBoxoffice : dailyBoxoffices) {
                if (dailyBoxoffice.getDayBoxoffice().compareTo(BigDecimal.ONE) < 0) break;
                DailyBoxofficeVO dailyBoxofficeVO = new DailyBoxofficeVO();
                BeanUtils.copyProperties(dailyBoxoffice,dailyBoxofficeVO);
                list.add(dailyBoxofficeVO);
            }
        }
        return list;
    }

    @Override
    @SneakyThrows
    public HistoygramVO getDatesHistoygram(String startDate, String endDate, Long movieCode) {
        if (movieCode == null){
            throw new ConditionException("查询条件异常！");
        }
        List<DailyBoxoffice> dailySumBoxoffices = this.baseMapper.selectList(new LambdaQueryWrapper<DailyBoxoffice>()
                .eq(DailyBoxoffice::getMovieCode,movieCode)
                .ge(DailyBoxoffice::getRecordDate, startDate)
                .le(DailyBoxoffice::getRecordDate, endDate)
                .orderByAsc(DailyBoxoffice::getRecordDate));
        HistoygramVO histoygramVO = new HistoygramVO();
        if (dailySumBoxoffices.size() > 0){
            histoygramVO.setXAxis(dailySumBoxoffices.stream().map(DailyBoxoffice::getRecordDate).collect(Collectors.toList()));
            List<BigDecimal> y =(dailySumBoxoffices.stream().map(DailyBoxoffice::getDayBoxoffice).collect(Collectors.toList()));
            List<String> yAxis = new ArrayList<>();
            for (BigDecimal bigDecimal : y) {
                yAxis.add(bigDecimal.toString());
            }
            histoygramVO.setYAxis(yAxis);
        }
        return histoygramVO;
    }

    @Override
    public HistoygramVO getWeekHistoygram(Long movieCode) {
        String startDate = getReleaseDate(movieCode);
        String endDate = MyDateUtils.getAddDate(startDate, MyDateUtils.YYMMDD, 7);
        return getDatesHistoygram(startDate, endDate, movieCode);
    }

    @Override
    public DailyBoxoffice latestBoxoffice(Long movieCode) {
        return this.baseMapper.selectList(new LambdaQueryWrapper<DailyBoxoffice>()
                .eq(DailyBoxoffice::getMovieCode,movieCode)
                .orderByDesc(DailyBoxoffice::getRecordDate)).get(0);
    }

    @Override
    public List<DailyBoxofficeVO> getDatesList(String startDate, String endDate) {
        List<DailyBoxoffice> dailyBoxoffices = this.baseMapper.selectList(new LambdaQueryWrapper<DailyBoxoffice>()
                .ge(DailyBoxoffice::getRecordDate, startDate)
                .le(DailyBoxoffice::getRecordDate, endDate)
                .ge(DailyBoxoffice::getDayBoxoffice, BigDecimal.ONE));
        List<DailyBoxofficeVO> list = new ArrayList<>();
        Map<Long, List<DailyBoxoffice>> map  ;
        if (dailyBoxoffices.size() > 0){
            map = dailyBoxoffices.stream().collect(Collectors.groupingBy(DailyBoxoffice::getMovieCode));
            map.forEach((k,v)->{
                DailyBoxofficeVO dailyBoxofficeVO = new DailyBoxofficeVO();
                if (v.size() > 1) {
                    BeanUtils.copyProperties(v.get(0), dailyBoxofficeVO);
                    BigDecimal sumBoxoffice = BigDecimal.ZERO;
                    for (DailyBoxoffice dailyBoxoffice : v) {
                        sumBoxoffice = sumBoxoffice.add(dailyBoxoffice.getDayBoxoffice());
                    }
                    dailyBoxofficeVO.setSumBoxoffice(sumBoxoffice.toString());
                }else {
                    BeanUtils.copyProperties(v.get(0), dailyBoxofficeVO);
                }
                list.add(dailyBoxofficeVO);
            });
        }
        list.sort((o1, o2) -> {
            if(o1.getDayBoxoffice().compareTo(o2.getDayBoxoffice()) < 0)
                return 1;
            else
                return -1;
        });
        return list.subList(0,20);
    }

    @Override
    public List<Long> getMoviesCodeList() {
        List<DailyBoxoffice> distinct = this.baseMapper.selectList(new QueryWrapper<DailyBoxoffice>().select("distinct movie_code"));
        return distinct.stream().map(DailyBoxoffice::getMovieCode).collect(Collectors.toList());
    }

    @Override
    public void verifyCode(Long oldCode, Long newCode) {
        this.baseMapper.verifyCode(oldCode,newCode);
    }


    private String getReleaseDate(Long movieCode) {
        DailyBoxoffice dailyBoxoffice = this.baseMapper.selectOne(new LambdaQueryWrapper<DailyBoxoffice>()
                        .eq(DailyBoxoffice::getMovieCode,movieCode)
                        .eq(DailyBoxoffice::getReleaseDays,1));
        if (dailyBoxoffice == null){
            dailyBoxoffice = this.baseMapper.selectList(new LambdaQueryWrapper<DailyBoxoffice>()
                    .eq(DailyBoxoffice::getMovieCode,movieCode)
                    .orderByAsc(DailyBoxoffice::getRecordDate)).get(0);
        }
        return dailyBoxoffice.getRecordDate();
    }
}
