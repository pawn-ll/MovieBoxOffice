package com.example.movieboxoffice.controller;

import com.example.movieboxoffice.entity.Response;
import com.example.movieboxoffice.entity.vo.DailyBoxofficeVO;
import com.example.movieboxoffice.entity.vo.HistoygramVO;
import com.example.movieboxoffice.service.impl.DailyBoxofficeServiceImpl;
import com.example.movieboxoffice.utils.MyDateUtils;
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
 * @since 2024-05-07
 */
@RestController
@RequestMapping("/dailyBoxoffice")
public class DailyBoxofficeController {

    @Autowired
    private DailyBoxofficeServiceImpl dailyBoxoffice;

    @GetMapping("/today")
    public Response<List<DailyBoxofficeVO>> getToday(){
        List<DailyBoxofficeVO> today = dailyBoxoffice.today();
        return Response.success(today);
    }
    @GetMapping("/histoygram")
    public Response<HistoygramVO> getDatesHistoygram( Long movieCode){
        String endDate = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);;
        String startDate = MyDateUtils.getAddDate(endDate, MyDateUtils.YYMMDD, -30);
        HistoygramVO histoygramVO = dailyBoxoffice.getDatesHistoygram(startDate, endDate, movieCode);
        return Response.success(histoygramVO);
    }

}
