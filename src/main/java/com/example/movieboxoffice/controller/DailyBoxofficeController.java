package com.example.movieboxoffice.controller;

import com.example.movieboxoffice.entity.vo.DailyBoxofficeVO;
import com.example.movieboxoffice.service.impl.DailyBoxofficeServiceImpl;
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
    public List<DailyBoxofficeVO> getToday(){
        return dailyBoxoffice.today();
    }

}
