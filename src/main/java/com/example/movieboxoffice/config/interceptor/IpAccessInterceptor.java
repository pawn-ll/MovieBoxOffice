package com.example.movieboxoffice.config.interceptor;


import com.example.movieboxoffice.service.IpAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class IpAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private IpAccessService ipAccessService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = request.getRemoteAddr();
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
