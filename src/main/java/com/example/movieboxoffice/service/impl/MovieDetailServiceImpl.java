package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.MovieDetail;
import com.example.movieboxoffice.entity.request.MovieDetailPageRequest;
import com.example.movieboxoffice.entity.vo.MovieDetailVO;
import com.example.movieboxoffice.mapper.MovieDetailMapper;
import com.example.movieboxoffice.service.IMovieDetailService;
import com.example.movieboxoffice.utils.Img2Base64Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-10
 */
@Service
public class MovieDetailServiceImpl extends ServiceImpl<MovieDetailMapper, MovieDetail> implements IMovieDetailService {


    @Autowired
    private DailyBoxofficeServiceImpl dailyBoxofficeService;

    private final static String PREFIX =  "data:image/jpeg;base64,";


    @Override
    public MovieDetailVO getDeatail(Long movieCode) {
        MovieDetail movieDetail = this.getOne(new LambdaQueryWrapper<MovieDetail>()
                .eq(MovieDetail::getMovieCode,movieCode));
        MovieDetailVO movieDetailVO = new MovieDetailVO();
        BeanUtils.copyProperties(movieDetail,movieDetailVO);
        movieDetailVO.setSumBoxOffice(dailyBoxofficeService.latestBoxoffice(movieCode).getSumBoxoffice());
        return movieDetailVO;
    }

    @Override
    public void deleteByCode(Long movieCode) {
        this.baseMapper.delete(new LambdaQueryWrapper<MovieDetail>()
                .eq(MovieDetail::getMovieCode,movieCode));
    }

    @Override
    public void setPosterBase64() {
        List<MovieDetail> list = getList();
        list.forEach(movieDetail -> {
            try {
                String base64 = Img2Base64Utils.imgToBase64(movieDetail.getPoster());
                if (!StringUtils.isEmpty(base64)) {
                    movieDetail.setPosterBase64(PREFIX + base64);
                    this.baseMapper.updateById(movieDetail);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Page<MovieDetail> getDetailBySearch(MovieDetailPageRequest request) {
        Page<MovieDetail> movieDetailPage = this.baseMapper.selectPage(
                new Page<>(request.getCurrent(), request.getSize()),
                new LambdaQueryWrapper<MovieDetail>()
                        .likeRight(MovieDetail::getMovieName, request.getMovieName()));

        return movieDetailPage;
    }


    private List<MovieDetail> getList(){
        IPage<MovieDetail> movieDetailIPage = this.baseMapper.selectPage(
                new Page<>(0,50),
                new LambdaQueryWrapper<MovieDetail>()
                        .isNull(MovieDetail::getPosterBase64)
                        .orderByAsc(MovieDetail::getId));
        return  movieDetailIPage.getRecords();
    }
}
