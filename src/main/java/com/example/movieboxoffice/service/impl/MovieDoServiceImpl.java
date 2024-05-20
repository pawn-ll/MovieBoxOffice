package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.MovieDo;
import com.example.movieboxoffice.mapper.MovieDoMapper;
import com.example.movieboxoffice.service.IMovieDoService;
import com.example.movieboxoffice.utils.MyDateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    public MovieDo selectByName(String name) {
        MovieDo movieDo = new MovieDo();
        movieDo.setMovieName(name);
        List<MovieDo> movieDos = this.baseMapper.selectList(new QueryWrapper<MovieDo>(movieDo));
        if (CollectionUtils.isEmpty(movieDos))
            return movieDos.get(0);
        else
            return null;

    }

    @Override
    public List<MovieDo> selectNotDO() {
        MovieDo movieDo = new MovieDo();
        movieDo.setIsDo(0);
        Page<MovieDo> page = new Page<>(1,20);
        return baseMapper.selectPage(page, new QueryWrapper<MovieDo>(movieDo)).getRecords();
    }

    @Override
    public void doMovie(Long movieCode) {
        MovieDo movieDo = baseMapper.selectOne(new QueryWrapper<MovieDo>().eq("movie_code",movieCode));
        if (movieDo !=null){
            movieDo.setIsDo(1);
            baseMapper.updateById(movieDo);
        }
    }

    @Override
    public List<MovieDo> selectTodayNotDO() {
        MovieDo movieDo = new MovieDo();
        movieDo.setIsDo(0);
        movieDo.setMovieDate(MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD));
        return baseMapper.selectList(new QueryWrapper<MovieDo>(movieDo));
    }
}
