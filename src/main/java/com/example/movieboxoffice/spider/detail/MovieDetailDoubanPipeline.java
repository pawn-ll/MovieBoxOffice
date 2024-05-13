package com.example.movieboxoffice.spider.detail;

import com.example.movieboxoffice.entity.MovieDetail;
import com.example.movieboxoffice.service.impl.MovieDetailServiceImpl;
import com.example.movieboxoffice.service.impl.MovieDoServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 *
 * @author walnut
 * @since  2024.5.12
 *
 */
@Component
@Log4j2
public class MovieDetailDoubanPipeline implements Pipeline {

    @Autowired
    private MovieDetailServiceImpl movieDetailService;;
    @Autowired
    private MovieDoServiceImpl movieDoService;


    @SneakyThrows
    @Override
    public void process(ResultItems resultItems, Task task) {
        String director = resultItems.get("director");
        String actor = resultItems.get("actor");
        String length = resultItems.get("length");
        String type = resultItems.get("type");
        String area = resultItems.get("area");
        String instruction = resultItems.get("instruction");
        String poster = resultItems.get("poster");
        if (director == null || actor == null || length == null) {
            log.error("爬取失败");
            return;
        }
        MovieDetail detail =new MovieDetail();
        detail.setMovieName(resultItems.get("movieName"));
        detail.setMovieCode(Long.parseLong(task.getUUID()));
        detail.setDirector(director);
        detail.setActor(actor);
        detail.setLength(length);
        detail.setType(type);
        detail.setArea(area);
        detail.setIntroduction(instruction);
        detail.setPoster(poster);
        movieDetailService.save(detail);
//        movieDoService.doMovie(detail.getMovieCode());

    }

}
