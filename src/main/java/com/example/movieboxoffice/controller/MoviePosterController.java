package com.example.movieboxoffice.controller;

import com.example.movieboxoffice.entity.Response;
import com.example.movieboxoffice.service.impl.MoviePosterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2024-06-26
 */
@RestController
@RequestMapping("/movie")
public class MoviePosterController {

    @Autowired
    private MoviePosterServiceImpl moviePosterService;

    @GetMapping("/poster")
    public Response<String> getMoviePoster(@RequestParam Long movieCode){

        return Response.success(moviePosterService.getPoster(movieCode));
    }

    @GetMapping("/poster/hot")
    public Response<Integer> hotMoviePoster(){
        return Response.success(moviePosterService.hotMoviePoster());
    }

}
