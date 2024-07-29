package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.MoviePoster;
import com.example.movieboxoffice.mapper.MoviePosterMapper;
import com.example.movieboxoffice.service.IMoviePosterService;
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
public class MoviePosterServiceImpl extends ServiceImpl<MoviePosterMapper, MoviePoster> implements IMoviePosterService {


    @Override
    public MoviePoster getPoster(Long movieCode) {
        return baseMapper.selectOne(new LambdaQueryWrapper<MoviePoster>()
                .eq(MoviePoster::getMovieCode, movieCode));
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
}
