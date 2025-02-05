package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.MoviePoster;
import com.example.movieboxoffice.mapper.MoviePosterMapper;
import com.example.movieboxoffice.service.IMoviePosterService;
import com.example.movieboxoffice.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-06-26
 */
@Service
@Log4j2
public class MoviePosterServiceImpl extends ServiceImpl<MoviePosterMapper, MoviePoster> implements IMoviePosterService {

    @Autowired
    private RedisService redisService;

    private static final Integer pageSize = 500 ;

    @Override
    public String getPoster(Long movieCode) {
        String poster = null;
        poster = (String) redisService.get(movieCode.toString());
        if (poster == null){
            poster = baseMapper.selectOne(new LambdaQueryWrapper<MoviePoster>()
                    .eq(MoviePoster::getMovieCode, movieCode)).getPosterBase64();
            redisService.set(movieCode.toString(),poster);
         }

        return poster;
    }

    @Override
    public List<MoviePoster> getNullPosterList(){
        IPage<MoviePoster> moviePage = this.baseMapper.selectPage(
                new Page<>(0,50),
                new LambdaQueryWrapper<MoviePoster>()
                        .isNull(MoviePoster::getPosterBase64));
        return  moviePage.getRecords();
    }

    @Override
    public Boolean isExists(String name) {
        return baseMapper.selectCount(
                new LambdaQueryWrapper<MoviePoster>().eq(MoviePoster::getMovieName,name))>0;
    }

    @Override
    public List<MoviePoster> getPosterList(Integer page, Integer size) {
        IPage<MoviePoster> moviePage = this.baseMapper.selectPage(
                new Page<>(page,size),
                new LambdaQueryWrapper<MoviePoster>()
                        .orderByAsc(MoviePoster::getCreateTime));
        return moviePage.getRecords();
    }

    @Override
    public Integer hotMoviePoster() {
        return hotPoster();
    }

    @Override
    public List<MoviePoster> selectList(List<Long> movieCodes) {
        return baseMapper.selectList(new LambdaQueryWrapper<MoviePoster>()
                .in(MoviePoster::getMovieCode, movieCodes));
    }

    private Integer hotPoster(){
        int count = 0;
        int page = 0;
        int size = pageSize;
        while (size == pageSize ){
            List<MoviePoster> moviePosterList = getPosterList(page++, pageSize);
            size = moviePosterList.size();
            count += size;
            for (MoviePoster moviePoster : moviePosterList) {
                redisService.set(moviePoster.getMovieCode().toString(),moviePoster.getPosterBase64());
            }
            log.info("已完成Page："+page);
        }
        return count;
    }

}
