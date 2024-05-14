package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.DailySumBoxoffice;
import com.example.movieboxoffice.entity.vo.DailySumBoxofficeVO;
import com.example.movieboxoffice.mapper.DailySumBoxofficeMapper;
import com.example.movieboxoffice.service.IDailySumBoxofficeService;
import com.example.movieboxoffice.utils.MyDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-07
 */
@Service
public class DailySumBoxofficeServiceImpl extends ServiceImpl<DailySumBoxofficeMapper, DailySumBoxoffice> implements IDailySumBoxofficeService {

    @Override
    public void deleteByDates(String startDate, String endDate) {
        LambdaQueryWrapper<DailySumBoxoffice> wrapper = new LambdaQueryWrapper<DailySumBoxoffice>()
                .ge(DailySumBoxoffice::getDate, startDate)
                .le(DailySumBoxoffice::getDate, endDate);
        this.baseMapper.delete(wrapper);

    }

    @Override
    public DailySumBoxofficeVO today() {
        DailySumBoxoffice dailySumBoxoffice = new DailySumBoxoffice();
        dailySumBoxoffice.setDate(MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD));
        dailySumBoxoffice = this.baseMapper.selectOne(new QueryWrapper<DailySumBoxoffice>(dailySumBoxoffice));
        DailySumBoxofficeVO sumBoxofficeVO = new DailySumBoxofficeVO();
        BeanUtils.copyProperties(dailySumBoxoffice, sumBoxofficeVO);
        return sumBoxofficeVO;
    }
}
