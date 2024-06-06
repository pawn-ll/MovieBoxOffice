package com.example.movieboxoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.MovieBoxoffice;
import com.example.movieboxoffice.entity.vo.MovieBoxofficeVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-20
 */
@Service
public interface IMovieBoxofficeService extends IService<MovieBoxoffice> {

    List<MovieBoxofficeVO> getTop20();

    void insertAll();

    MovieBoxoffice getByCode(Long movieCode);

}
