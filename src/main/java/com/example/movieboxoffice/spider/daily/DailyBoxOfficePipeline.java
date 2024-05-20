package com.example.movieboxoffice.spider.daily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.movieboxoffice.entity.BoxOfficeWeb;
import com.example.movieboxoffice.entity.DailyBoxoffice;
import com.example.movieboxoffice.entity.DailySumBoxoffice;
import com.example.movieboxoffice.entity.MovieDo;
import com.example.movieboxoffice.mapper.DailyBoxofficeMapper;
import com.example.movieboxoffice.mapper.DailySumBoxofficeMapper;
import com.example.movieboxoffice.service.IMovieDoService;
import com.github.yitter.idgen.YitIdHelper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.math.BigDecimal;
import java.util.List;

@Component
@Log4j2
public class DailyBoxOfficePipeline implements Pipeline {

    @Autowired
    private DailyBoxofficeMapper dailyBoxofficeMapper;
    @Autowired
    private DailySumBoxofficeMapper dailySumBoxofficeMapper;
    @Autowired
    @Qualifier("movieDoServiceImpl")
    private IMovieDoService movieDoService;


    @SneakyThrows
    @Override
    public void process(ResultItems resultItems, Task task) {
        String str = resultItems.get("list");
        String date = resultItems.get("date");

        List<BoxOfficeWeb> list = JSON.parseArray(str, BoxOfficeWeb.class);
        log.error("------------size:"+list.size());
        DailyBoxoffice dailyBoxoffice = new DailyBoxoffice();
        dailyBoxoffice.setRecordDate(date);
        for (BoxOfficeWeb boxOffice :list ){
            dailyBoxoffice.setId(null);
            dailyBoxoffice.setDayBoxoffice(parseBigDecimal(boxOffice.getSalesInWanDesc()));
            if (dailyBoxoffice.getDayBoxoffice().equals(BigDecimal.ZERO)) break;
            dailyBoxoffice.setDaySplitBoxoffice(parseBigDecimal(boxOffice.getSplitSalesInWanDesc()));
            dailyBoxoffice.setMovieName(boxOffice.getName());
            dailyBoxoffice.setSumBoxoffice(boxOffice.getSumSalesDesc());
            dailyBoxoffice.setSumSplitBoxoffice(boxOffice.getSumSplitSalesDesc());
            dailyBoxoffice.setReleaseDays(Integer.parseInt(boxOffice.getReleaseDays()));
            MovieDo movieDo = movieDoService.selectByName(dailyBoxoffice.getMovieName());
            long movieCode ;
            if (movieDo!=null) {
                movieCode =  movieDo.getMovieCode();
            }else {
                movieDo = new MovieDo();
                movieCode= YitIdHelper.nextId();
                movieDo.setMovieCode(movieCode);
                movieDo.setMovieName(dailyBoxoffice.getMovieName());
                movieDo.setMovieDate(date);
                movieDo.setMovieYear(Integer.parseInt(date.substring(0,4)));
                movieDoService.save(movieDo);
            }
            dailyBoxoffice.setMovieCode(movieCode);
            dailyBoxoffice.setDayBoxofficeRate(boxOffice.getSalesRateDesc());
            dailyBoxoffice.setDaySplitBoxofficeRate(boxOffice.getSplitSalesRateDesc());
            dailyBoxoffice.setDayArrangeRate(boxOffice.getSessionRateDesc());
            dailyBoxoffice.setDaySeatRate(boxOffice.getSeatRateDesc());


            dailyBoxofficeMapper.insert(dailyBoxoffice);

        }

        JSONObject nationalSales = resultItems.get("nationalSales");
        JSONObject salesDesc = (JSONObject)nationalSales.get("salesDesc");
        JSONObject splitSalesDesc = (JSONObject)nationalSales.get("splitSalesDesc");
        String sales = salesDesc.get("value").toString()+salesDesc.get("unit").toString();
        String splitSales = splitSalesDesc.get("value").toString()+splitSalesDesc.get("unit").toString();
        DailySumBoxoffice dailySumBoxoffice = new DailySumBoxoffice();
        dailySumBoxoffice.setSumBoxoffice(sales);
        dailySumBoxoffice.setSumSplitBoxoffice(splitSales);
        dailySumBoxoffice.setDate(date);
        dailySumBoxofficeMapper.insert(dailySumBoxoffice);

    }

    private BigDecimal parseBigDecimal(String s){
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(s);
        }catch (Exception e){
            bigDecimal = BigDecimal.ZERO;
        }
        return bigDecimal;

    }
}
