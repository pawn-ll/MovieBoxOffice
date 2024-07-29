package com.example.movieboxoffice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.MovieDetail;
import com.example.movieboxoffice.entity.request.MovieDetailPageRequest;
import com.example.movieboxoffice.entity.vo.MovieDetailVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-10
 */
public interface IMovieDetailService extends IService<MovieDetail> {


    MovieDetailVO getDeatail(Long movieCode);

    void deleteByCode(Long movieCode);

    void setPosterBase64();

    Page<MovieDetailVO> getDetailBySearch(MovieDetailPageRequest request);

    Boolean isExists(String name);
}
