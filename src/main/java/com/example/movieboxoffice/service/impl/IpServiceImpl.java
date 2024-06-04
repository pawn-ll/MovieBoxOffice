package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.movieboxoffice.entity.Ip;
import com.example.movieboxoffice.mapper.IpMapper;
import com.example.movieboxoffice.service.IIpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-06-04
 */
@Service
public class IpServiceImpl extends ServiceImpl<IpMapper, Ip> implements IIpService {

    @Override
    public List<Ip> getWhiteIpList() {
        return baseMapper.selectList(new LambdaQueryWrapper<Ip>().eq(Ip::getIsWhite, 1));
    }

    @Override
    public List<Ip> getBlackIpList() {
        return baseMapper.selectList(new LambdaQueryWrapper<Ip>().eq(Ip::getIsWhite, 0));
    }
}
