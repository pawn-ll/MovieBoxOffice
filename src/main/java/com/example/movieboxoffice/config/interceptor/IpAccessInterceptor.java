package com.example.movieboxoffice.config.interceptor;


import com.example.movieboxoffice.service.IpAccessService;
import com.example.movieboxoffice.service.RedisService;
import com.example.movieboxoffice.utils.MyConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Log4j2
public class IpAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private IpAccessService ipAccessService;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = request.getRemoteAddr();
        if(!redisService.sIsMember(MyConstant.WEB_SITE_VISITORS_TODAY, ipAddress)){
            redisService.sAdd(MyConstant.WEB_SITE_VISITORS_TODAY, ipAddress);

            Integer todayCount= (Integer)redisService.get(MyConstant.WEB_SITE_VISITOR_COUNT_TODAY);
            redisService.set(MyConstant.WEB_SITE_VISITOR_COUNT_TODAY, todayCount+1);

            Integer count= (Integer)redisService.get(MyConstant.WEB_SITE_VISITOR_COUNT);
            redisService.set(MyConstant.WEB_SITE_VISITOR_COUNT, count+1);

        }
        if(redisService.sIsMember(MyConstant.WHITE_IP_LIST, ipAddress)){
            return true;
        }
        if(redisService.sIsMember(MyConstant.BLACK_IP_LIST, ipAddress)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied for this IP address.");
            return false;
        }
        if (ipAccessService.isAccessRestricted(ipAddress)) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().write("Too many requests from this IP address.");
            return false;
        } else {
            ipAccessService.incrementAccessCount(ipAddress);
            return true;
        }
    }

}
