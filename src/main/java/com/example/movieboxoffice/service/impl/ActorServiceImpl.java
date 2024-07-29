package com.example.movieboxoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.movieboxoffice.entity.Actor;
import com.example.movieboxoffice.entity.MovieDetail;
import com.example.movieboxoffice.mapper.ActorMapper;
import com.example.movieboxoffice.service.IActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-07-01
 */
@Service
public class ActorServiceImpl extends ServiceImpl<ActorMapper, Actor> implements IActorService {

    @Autowired
    private MovieDetailServiceImpl movieDetailService;

    @Override
    public Boolean isExists(String name) {
        return baseMapper.selectCount(
                new LambdaQueryWrapper<Actor>().eq(Actor::getName,name))>0;
    }

    @Override
    public Page<Actor> getDoList() {
        Page<Actor> actorPage = baseMapper.selectPage(new Page<>(1, 1),
                new LambdaQueryWrapper<Actor>()
                        .isNotNull(Actor::getIntroduction)
                        .orderByDesc(Actor::getId));
        Integer id = null ;
        if (actorPage.getRecords().size()>0) {
            id = actorPage.getRecords().get(0).getId();
        }
        return baseMapper.selectPage(new Page<>(1,100),new LambdaQueryWrapper<Actor>()
                .isNull(Actor::getIntroduction)
                .gt(id!=null,Actor::getId,id)
                .orderByAsc(Actor::getId)
        );
    }

    @Override
    public List<Actor> existActor() {
        return baseMapper.selectList(new LambdaQueryWrapper<>());
    }



    @Override
    public void historyInsert() {
        int page = 1;
        int size = 500;
        List<Actor> existActor = existActor();
        Set<String> actorSet = existActor.stream().map(Actor::getName).collect(Collectors.toSet());
        while (size==500) {
            System.out.println(page);
            Page<MovieDetail> detailPage = movieDetailService.getDetailPage(page, size);
            size = detailPage.getRecords().size();
            page++;
            if (size > 0) {
                for (MovieDetail movieDetail : detailPage.getRecords()) {
                    String[] actors = movieDetail.getActor().split(" ");
                    Actor actor = null;
                    for (String a : actors) {
                        if (actorSet.contains(a)) {
                            continue;
                        }else {
                            actorSet.add(a);
                        }
                        actor = new Actor();
                        actor.setName(a);
                        save(actor);
                    }
                }
            }

        }
    }
}
