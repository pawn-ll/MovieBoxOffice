package com.example.movieboxoffice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieboxoffice.entity.Response;
import com.example.movieboxoffice.entity.request.MovieDetailPageRequest;
import com.example.movieboxoffice.entity.vo.MovieDetailVO;
import com.example.movieboxoffice.service.impl.MovieDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2024-05-10
 */
@RestController
@RequestMapping("/movie")
public class MovieDetailController {

    @Autowired
    private MovieDetailServiceImpl movieDetailService;

    @GetMapping("/detail")
    @ResponseBody
    public Response<MovieDetailVO> getMovieDetail(@RequestParam Long movieCode){
        MovieDetailVO movieDetailVO= movieDetailService.getDeatail(movieCode);
        return Response.success(movieDetailVO);
    }

    @PostMapping("/search")
    @ResponseBody
    public Response<Page<MovieDetailVO>> getDetailBySearch(@RequestBody MovieDetailPageRequest request){
        Page<MovieDetailVO> detailBySearch = movieDetailService.getDetailBySearch(request);
        return Response.success(detailBySearch);
    }

}
