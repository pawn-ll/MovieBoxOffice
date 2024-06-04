package com.example.movieboxoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.Ip;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-06-04
 */
public interface IIpService extends IService<Ip> {

    List<Ip> getWhiteIpList();

    List<Ip> getBlackIpList();

}
