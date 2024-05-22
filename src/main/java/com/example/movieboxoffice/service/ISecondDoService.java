package com.example.movieboxoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.SecondDo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-13
 */
public interface ISecondDoService extends IService<SecondDo> {

    List<SecondDo> getNotDOList();

    void deleteByCode(Long movieCode);

    void doMovie(Long movieCode);

}
