package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.MovieDo;
import com.example.movieboxoffice.mapper.MovieDoMapper;
import com.example.movieboxoffice.service.IMovieDoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-08
 */
@Service
public class MovieDoServiceImpl extends ServiceImpl<MovieDoMapper, MovieDo> implements IMovieDoService {


    @Override
    public MovieDo selectByNameAndYear(String name, Integer year) {
        MovieDo movieDo = new MovieDo();
        movieDo.setMovieName(name);
        movieDo.setMovieYear(year);
        return  this.baseMapper.selectOne(new QueryWrapper<MovieDo>(movieDo));

    }

    @Override
    public List<MovieDo> selectNotDO() {
        MovieDo movieDo = new MovieDo();
        movieDo.setIsDo(0);
        return this.baseMapper.selectList(new QueryWrapper<MovieDo>(movieDo));
    }
}
