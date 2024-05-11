package com.example.movieboxoffice.spider.detail;

import com.example.movieboxoffice.service.IMovieDoService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.math.BigDecimal;

@Component
@Log4j2
public class MovieDetailPipeline implements Pipeline {

    @Autowired
    @Qualifier("movieDoServiceImpl")
    private IMovieDoService movieDoService;


    @SneakyThrows
    @Override
    public void process(ResultItems resultItems, Task task) {
//        String str = resultItems.get("list");
//        String date = resultItems.get("date");
//
//        List<BoxOfficeWeb> list = JSON.parseArray(str, BoxOfficeWeb.class);

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
