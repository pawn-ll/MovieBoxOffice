package com.example.movieboxoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.MovieDetail;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-10
 */
public interface IMovieDetailService extends IService<MovieDetail> {

    void crawlDetail() throws InterruptedException, Exception;

}
