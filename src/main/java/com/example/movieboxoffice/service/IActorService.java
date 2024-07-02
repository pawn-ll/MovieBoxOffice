package com.example.movieboxoffice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.movieboxoffice.entity.Actor;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-07-01
 */
public interface IActorService extends IService<Actor> {

    void historyInsert();

    List<Actor> existActor();

    Page<Actor> getDoList();

}
