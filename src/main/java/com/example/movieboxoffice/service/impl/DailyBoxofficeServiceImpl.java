package com.example.movieboxoffice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.ConditionException;
import com.example.movieboxoffice.entity.DailyBoxoffice;
import com.example.movieboxoffice.entity.StatisBoxoffice;
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
import java.util.Date;
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
    @Autowired
    private StatisBoxofficeServiceImpl statisBoxofficeService;


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
        String s = (String) redisService.get(MyConstant.TODAY_DAILY_BOXOFFICELIST);
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
    public List<StatisBoxoffice> getDatesList(String startDate, String endDate) {
        List<StatisBoxoffice> statis = getStatis(startDate, endDate);
        return statis.subList(0,20);
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

    @Override
    public void statisDaily(Integer statisType) {
        String endDate = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);
        String startDate = "";
        String interval = getStatisInterval(statisType , endDate);
        if (statisType == 1){
            startDate = endDate.substring(0,8)+"01";
        }else if (statisType == 2){
            startDate = endDate.substring(0,5)+"01-01";
        }
        List<Map<String, Object>> statisData = getStatisData(startDate, endDate);
        if (statisData.size() > 0){
            statisData.forEach(map -> {
                Long movieCode = (Long) map.get("movie_code");
                StatisBoxoffice statisBoxoffice = statisBoxofficeService.getStatisBoxoffice(movieCode, statisType, interval);
                if (statisBoxoffice != null){
                    statisBoxoffice.setStatisSumBoxoffice(new BigDecimal(map.get("statis_sum_boxoffice").toString()));
                    statisBoxoffice.setAvgBoxofficeRate(map.get("avg_boxoffice_rate").toString()+"%");
                    statisBoxoffice.setAvgArrangeRate(map.get("avg_arrange_rate").toString()+"%");
                    statisBoxoffice.setAvgSeatRate(map.get("avg_seat_rate").toString()+"%");
                    statisBoxoffice.setUpdateTime(new Date());
                    statisBoxofficeService.updateById(statisBoxoffice);
                }else {
                    StatisBoxoffice newStatisBoxoffice = new StatisBoxoffice();
                    newStatisBoxoffice.setMovieCode(movieCode);
                    newStatisBoxoffice.setMovieName((String) map.get("movie_name"));
                    newStatisBoxoffice.setStatisSumBoxoffice(new BigDecimal(map.get("statis_sum_boxoffice").toString()));
                    newStatisBoxoffice.setAvgBoxofficeRate(map.get("avg_boxoffice_rate").toString()+"%");
                    newStatisBoxoffice.setAvgArrangeRate(map.get("avg_arrange_rate").toString()+"%");
                    newStatisBoxoffice.setAvgSeatRate(map.get("avg_seat_rate").toString()+"%");
                    newStatisBoxoffice.setStatisInterval(interval);
                    newStatisBoxoffice.setStatisType(statisType);
                    statisBoxofficeService.save(newStatisBoxoffice);
                }
            });
        }
    }

    @Override
    public void statisHistory(Integer statisType, String startDate, String endDate) {
        List<Map<String, Object>> statisData = getStatisData(startDate, endDate);
        if (statisData.size() > 0){
            statisData.forEach(map -> {
                StatisBoxoffice statisBoxoffice = new StatisBoxoffice();
                statisBoxoffice.setMovieCode((Long) map.get("movie_code"));
                statisBoxoffice.setMovieName((String) map.get("movie_name"));
                statisBoxoffice.setStatisSumBoxoffice(new BigDecimal(map.get("statis_sum_boxoffice").toString()));
                statisBoxoffice.setAvgBoxofficeRate(map.get("avg_boxoffice_rate").toString()+"%");
                statisBoxoffice.setAvgArrangeRate(map.get("avg_arrange_rate").toString()+"%");
                statisBoxoffice.setAvgSeatRate(map.get("avg_seat_rate").toString()+"%");
                statisBoxoffice.setStatisInterval(getStatisInterval(statisType , startDate));
                statisBoxoffice.setStatisType(statisType);
                statisBoxofficeService.save(statisBoxoffice);
            });
        }
    }

    @Override
    public List<DailyBoxofficeVO> day(String date) {
        List<DailyBoxofficeVO> list = new ArrayList<>();
        if (MyDateUtils.afterNowDate(date , MyDateUtils.YYMMDD)){
            date = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);
        }
        List<DailyBoxoffice> dailyBoxoffices = this.baseMapper.selectList(new LambdaQueryWrapper<DailyBoxoffice>()
                .eq(DailyBoxoffice::getRecordDate, date));

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
    public List<StatisBoxoffice> getStatis(String startDate, String endDate) {
        List<Map<String, Object>> statisData = getStatisData(startDate, endDate);
        List<StatisBoxoffice> list = new ArrayList<>();
        if (statisData.size() > 0){
            statisData.forEach(map -> {
                StatisBoxoffice statisBoxoffice = new StatisBoxoffice();
                statisBoxoffice.setMovieCode((Long) map.get("movie_code"));
                statisBoxoffice.setMovieName((String) map.get("movie_name"));
                statisBoxoffice.setStatisSumBoxoffice(new BigDecimal(map.get("statis_sum_boxoffice").toString()));
                statisBoxoffice.setAvgBoxofficeRate(map.get("avg_boxoffice_rate").toString() + "%");
                statisBoxoffice.setAvgArrangeRate(map.get("avg_arrange_rate").toString() + "%");
                statisBoxoffice.setAvgSeatRate(map.get("avg_seat_rate").toString() + "%");
                if(statisBoxoffice.getStatisSumBoxoffice().compareTo(new BigDecimal(1))>0) {
                    list.add(statisBoxoffice);
                }
            });
        }
        return list;
    }

    private String getStatisInterval(Integer statisType, String date){
        if (statisType == 1){
            return date.substring(0,7);
        }else if (statisType == 2){
            return date.substring(0,4);
        }else {
            return null;
        }
    }
    private List<Map<String, Object>> getStatisData(String startDate, String endDate){
        QueryWrapper<DailyBoxoffice> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .ge("record_date", startDate) // Greater than or equal to start date
                .le("record_date", endDate) // Less than or equal to end date
                .select("movie_code", "movie_name", "SUM(day_boxoffice) statis_sum_boxoffice",
                        "ROUND(AVG(day_boxoffice_rate), 2) avg_boxoffice_rate",
                        "ROUND(AVG(day_arrange_rate), 2) avg_arrange_rate",
                        "ROUND(AVG(day_seat_rate), 2) avg_seat_rate")
                .groupBy("movie_code")
                .orderByDesc("statis_sum_boxoffice"); // Order by sumBoxOffice in descending order
         return baseMapper.selectMaps(queryWrapper);
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
