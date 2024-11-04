package com.example.movieboxoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.StatisBoxoffice;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-06-05
 */
@Service
public interface IStatisBoxofficeService extends IService<StatisBoxoffice> {

    StatisBoxoffice getStatisBoxoffice(Long movieCode, Integer statisType , String statisInterval);

    List<StatisBoxoffice> getStatisList(Integer statisType, String statisInterval);

    void outputStatis(List<StatisBoxoffice> list , HttpServletResponse response) throws IOException;
}
