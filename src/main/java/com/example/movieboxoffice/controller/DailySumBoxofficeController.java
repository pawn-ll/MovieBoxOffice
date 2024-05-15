package com.example.movieboxoffice.controller;

import com.example.movieboxoffice.entity.Response;
import com.example.movieboxoffice.entity.vo.DailySumBoxofficeVO;
import com.example.movieboxoffice.service.impl.DailySumBoxofficeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/today")
    public Response<DailySumBoxofficeVO> getToday(){
        DailySumBoxofficeVO today = dailySumBoxoffice.today();
        return Response.success(today);
    }

}
