package com.example.movieboxoffice.controller;

import com.example.movieboxoffice.entity.Response;
import com.example.movieboxoffice.entity.vo.SiteVisitorCountVO;
import com.example.movieboxoffice.service.RedisService;
import com.example.movieboxoffice.utils.MyConstant;
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
 * @since 2024-06-18
 */
@RestController
@RequestMapping("/site")
public class SiteVisitorCountController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/count")
    public Response<SiteVisitorCountVO> getSiteVisitorCount(){
        SiteVisitorCountVO siteVisitorCountVO = new SiteVisitorCountVO();
        siteVisitorCountVO.setSiteVisitorCount((Integer) redisService.get(MyConstant.WEB_SITE_VISITOR_COUNT));
        siteVisitorCountVO.setSiteVisitorTodayCount((Integer) redisService.get(MyConstant.WEB_SITE_VISITOR_COUNT_TODAY));
        return Response.success(siteVisitorCountVO);
    }

}
