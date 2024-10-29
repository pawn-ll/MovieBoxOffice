package com.example.movieboxoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.MoviePoster;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-06-26
 */
public interface IMoviePosterService extends IService<MoviePoster> {

    String getPoster(Long movieCode);

    List<MoviePoster> getNullPosterList();

    Boolean isExists(String name);

    List<MoviePoster> getPosterList(Integer page, Integer size);

    Integer hotMoviePoster();
}
