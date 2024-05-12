package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.MovieDetail;
import com.example.movieboxoffice.entity.MovieDo;
import com.example.movieboxoffice.mapper.MovieDetailMapper;
import com.example.movieboxoffice.service.IMovieDetailService;
import com.example.movieboxoffice.spider.detail.MovieDetailSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-10
 */
@Service
public class MovieDetailServiceImpl extends ServiceImpl<MovieDetailMapper, MovieDetail> implements IMovieDetailService {

    @Autowired
    private MovieDetailSpider spider;
    @Autowired
    private MovieDoServiceImpl movieDoService;


    @Override
    public void crawlDetail()  {
//        List<MovieDo> movieDos = movieDoService.selectNotDO();
//        for (MovieDo movieDo : movieDos){
//            spider.getDefaultSpider(movieDo).run();
//            System.out.println("--------------休息-----------------");
//            Thread.sleep(1000*3);
//        }
        MovieDo movieDo1 = movieDoService.getBaseMapper().selectById(11);
        spider.getDefaultSpider(movieDo1).run();

    }
}
