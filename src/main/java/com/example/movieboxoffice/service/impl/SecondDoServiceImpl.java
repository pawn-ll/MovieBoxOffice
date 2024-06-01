package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.SecondDo;
import com.example.movieboxoffice.mapper.SecondDoMapper;
import com.example.movieboxoffice.service.ISecondDoService;
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
        Page<SecondDo> page = this.baseMapper.selectPage(new Page<>(0, 10), new LambdaQueryWrapper<SecondDo>()
                .eq(SecondDo::getIsDo, 1)
                .orderByAsc(SecondDo::getId));
        Integer id = null;
        if (page.getRecords().size()>0){
            id = page.getRecords().get(0).getId();
        }
        Page<SecondDo> secondDoPage = this.baseMapper.selectPage(new Page<>(0, 70), new LambdaQueryWrapper<SecondDo>()
                .eq(SecondDo::getIsDo, 0)
                .lt(id != null,SecondDo::getId,id)
                .orderByDesc(SecondDo::getId));
        return secondDoPage.getRecords();
    }

    @Override
    public void deleteByCode(Long movieCode) {
        this.baseMapper.delete(new LambdaQueryWrapper<SecondDo>()
                .eq(SecondDo::getMovieCode,movieCode));
    }

    @Override
    public void doMovie(Long movieCode) {
        SecondDo secondDo = this.baseMapper.selectOne(new LambdaQueryWrapper<SecondDo>()
                .eq(SecondDo::getMovieCode, movieCode));
        secondDo.setIsDo(1);
        baseMapper.updateById(secondDo);
    }

    @Override
    public List<SecondDo> selectUrlList() {
        return baseMapper.selectPage(new Page<>(0,50),
                new LambdaQueryWrapper<SecondDo>()
                        .eq(SecondDo::getIsDo,0)
                        .likeRight(SecondDo::getDetailUrl,"https://movie.douban.com"))
                .getRecords();
    }
}
