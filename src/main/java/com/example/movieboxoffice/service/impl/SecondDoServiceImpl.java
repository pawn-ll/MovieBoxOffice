package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.movieboxoffice.entity.SecondDo;
import com.example.movieboxoffice.mapper.SecondDoMapper;
import com.example.movieboxoffice.service.ISecondDoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-13
 */
@Service
public class SecondDoServiceImpl extends ServiceImpl<SecondDoMapper, SecondDo> implements ISecondDoService {

    @Override
    public List<SecondDo> getNotDOList() {
        return this.baseMapper.selectList(new LambdaQueryWrapper<SecondDo>()
                .eq(SecondDo::getIsDo,0)
                .orderByDesc(SecondDo::getId)
        );
    }

    @Override
    public void deleteByCode(Long movieCode) {
        this.baseMapper.delete(new LambdaQueryWrapper<SecondDo>()
                .eq(SecondDo::getMovieCode,movieCode));
    }
}
