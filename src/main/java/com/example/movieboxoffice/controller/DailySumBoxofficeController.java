package com.example.movieboxoffice.controller;

import com.example.movieboxoffice.entity.Response;
import com.example.movieboxoffice.entity.vo.DailySumBoxofficeVO;
import com.example.movieboxoffice.entity.vo.HistoygramVO;
import com.example.movieboxoffice.service.impl.DailySumBoxofficeServiceImpl;
import com.example.movieboxoffice.utils.MyDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2024-05-07
 */
@RestController
@RequestMapping("/dailySumBoxoffice")
public class DailySumBoxofficeController {

    @Autowired
    private DailySumBoxofficeServiceImpl dailySumBoxoffice;
    @Value("${recentDays}")
    private Integer days;

    @GetMapping("/today")
    @ResponseBody
    public Response<DailySumBoxofficeVO> getToday(){
        DailySumBoxofficeVO today = dailySumBoxoffice.today();
        return Response.success(today);
    }

    @GetMapping("/histoygram")
    public Response<HistoygramVO> getDatesHistoygram(){
        String endDate = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);;
        String startDate = MyDateUtils.getAddDate(endDate, MyDateUtils.YYMMDD, -days);
        HistoygramVO histoygramVO = dailySumBoxoffice.getDatesHistoygram(startDate, endDate);
        return Response.success(histoygramVO);
    }

    @GetMapping("/day")
    @ResponseBody
    public Response<DailySumBoxofficeVO> getDay(String date){
        DailySumBoxofficeVO today = dailySumBoxoffice.day(date);
        return Response.success(today);
    }
}
