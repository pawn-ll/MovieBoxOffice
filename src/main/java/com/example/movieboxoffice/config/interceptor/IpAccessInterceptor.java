package com.example.movieboxoffice.config.interceptor;


import com.example.movieboxoffice.service.IpAccessService;
import com.example.movieboxoffice.service.RedisService;
import com.example.movieboxoffice.utils.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class IpAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private IpAccessService ipAccessService;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = request.getRemoteAddr();
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
