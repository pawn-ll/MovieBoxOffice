package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.MovieDetail;
import com.example.movieboxoffice.entity.vo.MovieDetailVO;
import com.example.movieboxoffice.mapper.MovieDetailMapper;
import com.example.movieboxoffice.service.IMovieDetailService;
import org.springframework.beans.BeanUtils;
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
    private MovieDoServiceImpl movieDoService;
    @Autowired
    private DailyBoxofficeServiceImpl dailyBoxofficeService;


    @Override
    public void crawlDetail()  {
//        List<MovieDo> movieDos = movieDoService.selectNotDO();
//        for (MovieDo movieDo : movieDos){
//        }


    }

    @Override
    public MovieDetailVO getDeatail(Long movieCode) {
        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setMovieCode(movieCode);
        movieDetail = this.getOne(new LambdaQueryWrapper<MovieDetail>(movieDetail));
        MovieDetailVO movieDetailVO = new MovieDetailVO();
        BeanUtils.copyProperties(movieDetail,movieDetailVO);
        movieDetailVO.setSumBoxOffice(dailyBoxofficeService.latestBoxoffice(movieCode).getSumBoxoffice());
        return movieDetailVO;
    }

    @Override
    public void deleteByCode(Long movieCode) {
        this.baseMapper.delete(new LambdaQueryWrapper<MovieDetail>()
                .eq(MovieDetail::getMovieCode,movieCode));
    }
}
