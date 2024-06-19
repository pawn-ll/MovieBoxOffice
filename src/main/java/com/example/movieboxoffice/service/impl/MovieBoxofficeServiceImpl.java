package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.DailyBoxoffice;
import com.example.movieboxoffice.entity.MovieBoxoffice;
import com.example.movieboxoffice.entity.vo.MovieBoxofficeVO;
import com.example.movieboxoffice.mapper.MovieBoxofficeMapper;
import com.example.movieboxoffice.service.IMovieBoxofficeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-20
 */
@Service
public class MovieBoxofficeServiceImpl extends ServiceImpl<MovieBoxofficeMapper, MovieBoxoffice> implements IMovieBoxofficeService {

    @Autowired
    private DailyBoxofficeServiceImpl dailyBoxofficeService;

    @Override
    public List<MovieBoxoffice> getExistList() {
        return baseMapper.selectList(new QueryWrapper<MovieBoxoffice>());
    }

    @Override
    public List<MovieBoxofficeVO> getTop20() {
        Page<MovieBoxoffice> movieBoxofficePage = this.baseMapper.selectPage(new Page<MovieBoxoffice>(0, 20), new LambdaQueryWrapper<MovieBoxoffice>()
                .orderByDesc(MovieBoxoffice::getSumBoxoffice));
        List<MovieBoxoffice> records = movieBoxofficePage.getRecords();
        List<MovieBoxofficeVO> list = new ArrayList<>();
        MovieBoxofficeVO movieBoxofficeVO;
        if (records.size() > 0) {
            for (MovieBoxoffice movie : records){
                movieBoxofficeVO = new MovieBoxofficeVO();
                BeanUtils.copyProperties(movie , movieBoxofficeVO);
                list.add(movieBoxofficeVO);
            }
        }
        return list;
    }

    @Override
    public void insertAll()  {
        List<Long> moviesCodeList = dailyBoxofficeService.getMoviesCodeList();
        List<MovieBoxoffice> movieBoxoffices = this.baseMapper.selectList(new QueryWrapper<>());
        List<Long> existCodeList = movieBoxoffices.stream().map(MovieBoxoffice::getMovieCode).collect(Collectors.toList());
        moviesCodeList.removeAll(existCodeList);
        MovieBoxoffice movieBoxoffice = null;
        for (Long code : moviesCodeList){
            DailyBoxoffice dailyBoxoffice = dailyBoxofficeService.latestBoxoffice(code);
            movieBoxoffice = new MovieBoxoffice();
            movieBoxoffice.setMovieName(dailyBoxoffice.getMovieName());
            movieBoxoffice.setMovieCode(dailyBoxoffice.getMovieCode());
            try {
                movieBoxoffice.setSumBoxoffice(convertBoxoffice(dailyBoxoffice.getSumBoxoffice()));
                movieBoxoffice.setSumSplitBoxoffice(convertBoxoffice(dailyBoxoffice.getSumSplitBoxoffice()));
            }catch (Exception e){
                System.out.println(dailyBoxoffice.getMovieName()+" : "+dailyBoxoffice.getMovieCode());
            }
            movieBoxoffice.setUpdateTime(new Date());
            this.save(movieBoxoffice);
        }
    }

    @Override
    public MovieBoxoffice getByCode(Long movieCode) {
        return baseMapper.selectOne(new QueryWrapper<MovieBoxoffice>().eq("movie_code",movieCode));
    }

    public BigDecimal convertBoxoffice(String boxoffice){
        int length = boxoffice.length();
        BigDecimal res ;
        if (boxoffice.charAt(length - 1) == '万'){
            res =  new BigDecimal(boxoffice.substring(0,length-1));
        }else if (boxoffice.charAt(length - 1) == '亿'){
            int index = boxoffice.indexOf(".");
            BigDecimal sub = new BigDecimal(boxoffice.substring(index, index + 3));
            BigDecimal bigDecimal = new BigDecimal(boxoffice.substring(0, index));
            res =  bigDecimal.multiply(new BigDecimal(10000)).add(sub);
        }else {
            res = BigDecimal.ZERO;
        }
        return res;
    }

}
