package com.example.movieboxoffice.service;

import com.example.movieboxoffice.entity.MovieDo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-08
 */
public interface IMovieDoService extends IService<MovieDo> {

    MovieDo selectByNameAndYear(String name ,Integer year);

    List<MovieDo> selectNotDO();

    void doMovie(Long movieCode);

}
