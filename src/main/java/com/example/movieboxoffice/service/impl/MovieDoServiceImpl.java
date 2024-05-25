package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.MovieDo;
import com.example.movieboxoffice.mapper.MovieDoMapper;
import com.example.movieboxoffice.service.IMovieDoService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SecondDoServiceImpl secondDoService;
    @Autowired
    private MovieDetailServiceImpl movieDetailService;
    @Autowired
    private DailyBoxofficeServiceImpl dailyBoxofficeService;

    @Override
    public List<MovieDo> selectExistList() {

        return this.baseMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<MovieDo> selectNotDO() {
        Page<MovieDo> page = new Page<>(1,70);
        return baseMapper.selectPage(page, new LambdaQueryWrapper<MovieDo>()
                .eq(MovieDo::getIsDo,0))
                .getRecords();
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
    public void verifyMovieCode() {
        List<MovieDo> movieDos = this.baseMapper.selectList(
                new LambdaQueryWrapper<MovieDo>()
                        .groupBy(MovieDo::getMovieName).having("count(1)>1"));
        int count =0;
        for (MovieDo movieDo : movieDos ){
            List<MovieDo> list = this.baseMapper.selectList(new LambdaQueryWrapper<MovieDo>()
                    .eq(MovieDo::getMovieName, movieDo.getMovieName())
                    .orderByAsc(MovieDo::getId));
            int size = list.size();
            Long newCode = list.get(0).getMovieCode();
            MovieDo movie = null;
            for (int i = 1;i<size;i++){
                movie = list.get(i);
                Long oldCode = movie.getMovieCode();
                try {
                    /**
                     * 1、 删 secondDo
                     * 2、 删 detail
                     * 3、 更新 dailyBoxOffice
                     * 4、 删 movieDo
                     */
                    secondDoService.deleteByCode(oldCode);
                    movieDetailService.deleteByCode(oldCode);
                    dailyBoxofficeService.verifyCode(oldCode,newCode);
                    deleteByCode(oldCode);

                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("---------异常----------");
                    System.out.println(movie.getMovieCode()+" : "+movie.getMovieName());
                }
            }
            if (count > 100 ){
                break;
            }
            count++;
            System.out.println(count);
        }

    }

    private void deleteByCode(Long movieCode){
        baseMapper.delete(new LambdaQueryWrapper<MovieDo>().eq(MovieDo::getMovieCode,movieCode));
    }

}
