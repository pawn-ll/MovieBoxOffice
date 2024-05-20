package com.example.movieboxoffice.controller;

import com.example.movieboxoffice.entity.Response;
import com.example.movieboxoffice.entity.vo.MovieBoxofficeVO;
import com.example.movieboxoffice.service.impl.MovieBoxofficeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2024-05-20
 */
@RestController
@RequestMapping("/movieBoxoffice")
public class MovieBoxofficeController {

    @Autowired
    private MovieBoxofficeServiceImpl movieBoxofficeService;

    @GetMapping("top20")
    public Response<List<MovieBoxofficeVO>> getTop20(){
        List<MovieBoxofficeVO> top20 = movieBoxofficeService.getTop20();
        return Response.success(top20);
    }

}
