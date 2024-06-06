package com.example.movieboxoffice.controller;

import com.example.movieboxoffice.entity.Response;
import com.example.movieboxoffice.entity.StatisBoxoffice;
import com.example.movieboxoffice.entity.request.StatisRequest;
import com.example.movieboxoffice.service.impl.DailyBoxofficeServiceImpl;
import com.example.movieboxoffice.service.impl.StatisBoxofficeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2024-06-05
 */
@RestController
@RequestMapping("/statisBoxoffice")
public class StatisBoxofficeController {

    @Autowired
    private StatisBoxofficeServiceImpl statisBoxofficeService;
    @Autowired
    private DailyBoxofficeServiceImpl dailyBoxofficeService;

    @PostMapping("/statis")
    @ResponseBody
    public Response<List<StatisBoxoffice>> getStatis(@RequestBody StatisRequest request){
        List<StatisBoxoffice> list =null;
        if (request.getStatisType() == 0 ){
            list = dailyBoxofficeService.getStatis(request.getStartDate() , request.getEndDate());
        }else {
            list = statisBoxofficeService.getStatisList(request.getStatisType(), request.getStatisInterval());
        }
        return Response.success(list);
    }

}
