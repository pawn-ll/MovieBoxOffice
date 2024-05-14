package com.example.movieboxoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.DailySumBoxoffice;
import com.example.movieboxoffice.entity.vo.DailySumBoxofficeVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-07
 */
public interface IDailySumBoxofficeService extends IService<DailySumBoxoffice> {

    void deleteByDates(String startDate , String endDate);

    DailySumBoxofficeVO today() ;


}
