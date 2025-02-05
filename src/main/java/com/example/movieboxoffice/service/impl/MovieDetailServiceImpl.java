package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.MovieDetail;
import com.example.movieboxoffice.entity.MoviePoster;
import com.example.movieboxoffice.entity.request.MovieDetailPageRequest;
import com.example.movieboxoffice.entity.vo.MovieDetailVO;
import com.example.movieboxoffice.mapper.MovieDetailMapper;
import com.example.movieboxoffice.service.IMovieDetailService;
import com.example.movieboxoffice.service.RedisService;
import com.example.movieboxoffice.utils.Img2Base64Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private MovieBoxofficeServiceImpl movieBoxofficeService;
    @Autowired
    private MoviePosterServiceImpl moviePosterService;
    @Autowired
    private RedisService redisService;

    private final static String PREFIX =  "data:image/jpeg;base64,";


    @Override
    public MovieDetailVO getDeatail(Long movieCode) {
        MovieDetail movieDetail = this.getOne(new LambdaQueryWrapper<MovieDetail>()
                .eq(MovieDetail::getMovieCode,movieCode));
        MovieDetailVO movieDetailVO = new MovieDetailVO();
        if (movieDetail == null) {
            return null;
        }
        BeanUtils.copyProperties(movieDetail,movieDetailVO);
        movieDetailVO.setSumBoxOffice(movieBoxofficeService.getByCode(movieCode).getSumBoxoffice().toString());
        return movieDetailVO;
    }

    @Override
    public void deleteByCode(Long movieCode) {
        this.baseMapper.delete(new LambdaQueryWrapper<MovieDetail>()
                .eq(MovieDetail::getMovieCode,movieCode));
    }

    @Override
    public void setPosterBase64() {
        List<MoviePoster> nullPosterList = moviePosterService.getNullPosterList();
        nullPosterList.forEach(movie -> {
            try {
                MovieDetailVO detail = getDeatail(movie.getMovieCode());
                String base64 = Img2Base64Utils.imgToBase64(detail.getPoster());
                if (!StringUtils.isEmpty(base64)) {
                    movie.setPosterBase64(PREFIX + base64);
                    moviePosterService.update(movie ,new LambdaQueryWrapper<MoviePoster>()
                            .eq(MoviePoster::getMovieCode,movie.getMovieCode()));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Boolean isExists(String name) {
        return baseMapper.selectCount(
                new LambdaQueryWrapper<MovieDetail>().eq(MovieDetail::getMovieName,name))>0;
    }

    @Override
    public Page<MovieDetailVO> getDetailBySearch(MovieDetailPageRequest request) {
        Page<MovieDetail> movieDetailPage = this.baseMapper.selectPage(
                new Page<>(request.getCurrent(), request.getSize()),
                new LambdaQueryWrapper<MovieDetail>()
                        .likeRight(MovieDetail::getMovieName, request.getMovieName()));

        // 提前获取所有需要的海报数据
        List<Long> movieCodes = movieDetailPage.getRecords().stream()
                .map(MovieDetail::getMovieCode)
                .collect(Collectors.toList());

        Map<Long, String> posterMap = fetchPostersInBatch(movieCodes);


        Page<MovieDetailVO> movieDetailVOPage = new Page<>(request.getCurrent(), request.getSize());
        List<MovieDetailVO> movieDetailVOList =  new ArrayList<>();
        if (movieDetailPage.getRecords().size()>0){
            movieDetailVOList = movieDetailPage.getRecords().stream()
                    .map(record -> {
                        MovieDetailVO movieDetailVO = new MovieDetailVO();
                        BeanUtils.copyProperties(record, movieDetailVO);
                        movieDetailVO.setPosterBase64(posterMap.get(record.getMovieCode()));
                        return movieDetailVO;
                    })
                    .collect(Collectors.toList());
        }

        movieDetailVOPage.setRecords(movieDetailVOList);
        movieDetailVOPage.setTotal(movieDetailPage.getTotal());
        movieDetailVOPage.setPages(movieDetailPage.getPages());
        movieDetailVOPage.setCurrent(movieDetailPage.getCurrent());
        movieDetailVOPage.setSize(movieDetailPage.getSize());
        return movieDetailVOPage;
    }


    public Page<MovieDetail> getDetailPage(int current, int size ) {
        Page<MovieDetail> movieDetailPage = this.baseMapper.selectPage(
                new Page<>(current, size),
                new LambdaQueryWrapper<MovieDetail>());

        return movieDetailPage;
    }


    // 批量获取海报数据的方法
    private Map<Long, String> fetchPostersInBatch(List<Long> movieCodes) {
        Map<Long, String> posterMap = new HashMap<>();

        // 从缓存中获取海报数据
        List<Object> cachedPosters = redisService.mget(movieCodes.stream().map(Object::toString).collect(Collectors.toList()));
        for (int i = 0; i < movieCodes.size(); i++) {
            Long movieCode = movieCodes.get(i);
            String poster = (String)cachedPosters.get(i);
            if (poster != null) {
                posterMap.put(movieCode, poster);
            }
        }

        // 获取未命中缓存的海报数据
        List<Long> missingMovieCodes = movieCodes.stream()
                .filter(movieCode -> !posterMap.containsKey(movieCode))
                .collect(Collectors.toList());

        if (!missingMovieCodes.isEmpty()) {
            List<MoviePoster> missingPosters = moviePosterService.selectList(missingMovieCodes);
            for (MoviePoster poster : missingPosters) {
                posterMap.put(poster.getMovieCode(), poster.getPosterBase64());
                redisService.set(poster.getMovieCode().toString(), poster.getPosterBase64());
            }
        }

        return posterMap;
    }


    public List<MovieDetail> getDetails(String time){
        if(time == null)
            return null;
        return this.baseMapper.selectList(new LambdaQueryWrapper<MovieDetail>()
                .ge(MovieDetail::getCreateTime,time));
    }
//    private List<MovieDetail> getList(){
//        IPage<MovieDetail> movieDetailIPage = this.baseMapper.selectPage(
//                new Page<>(0,50),
//                new LambdaQueryWrapper<MovieDetail>()
//                        .isNull(MovieDetail::getPosterBase64)
//                        .orderByAsc(MovieDetail::getId));
//        return  movieDetailIPage.getRecords();
//    }
}
