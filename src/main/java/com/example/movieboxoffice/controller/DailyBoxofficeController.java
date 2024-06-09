package com.example.movieboxoffice.controller;

import com.example.movieboxoffice.entity.Response;
import com.example.movieboxoffice.entity.StatisBoxoffice;
import com.example.movieboxoffice.entity.vo.DailyBoxofficeVO;
import com.example.movieboxoffice.entity.vo.HistoygramVO;
import com.example.movieboxoffice.service.impl.DailyBoxofficeServiceImpl;
import com.example.movieboxoffice.utils.MyDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Response<HistoygramVO> getDatesHistoygram(@RequestParam Long movieCode){
        String endDate = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);;
        String startDate = MyDateUtils.getAddDate(endDate, MyDateUtils.YYMMDD, -15);
        HistoygramVO histoygramVO = dailyBoxoffice.getDatesHistoygram(startDate, endDate, movieCode);
        return Response.success(histoygramVO);
    }

    @GetMapping("/week-histoygram")
    public Response<HistoygramVO> getWeekHistoygram(@RequestParam Long movieCode){
        HistoygramVO histoygramVO = dailyBoxoffice.getWeekHistoygram( movieCode);
        return Response.success(histoygramVO);
    }

    @GetMapping("/week-list")
    public Response<List<StatisBoxoffice>> getWeekList(String startDate , String endDate){
        if (startDate ==null || endDate ==null){
            endDate = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);
            startDate = MyDateUtils.getAddDate(endDate , MyDateUtils.YYMMDD, -7);
        }
        List<StatisBoxoffice> datesList = dailyBoxoffice.getDatesList(startDate, endDate);
        return Response.success(datesList);
    }

    @GetMapping("/day")
    @ResponseBody
    public Response<List<DailyBoxofficeVO>> getday(String date){
        List<DailyBoxofficeVO> today = dailyBoxoffice.day(date);
        return Response.success(today);
    }

}
