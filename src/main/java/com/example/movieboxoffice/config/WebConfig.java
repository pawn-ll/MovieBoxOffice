package com.example.movieboxoffice.config;

import com.example.movieboxoffice.config.interceptor.IpAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private IpAccessInterceptor ipAccessInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 适用于所有路径
                .allowedOrigins("http://localhost:5173/", "http://1.14.58.251:5173/","http://116.198.228.160:5173/") // 允许任何来源
                .allowedMethods("*") // 允许任何HTTP方法
                .allowedHeaders("*") // 允许任何头部
                .allowCredentials(true) // 允许证书
                .maxAge(3600); // 预检请求的有效期
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipAccessInterceptor);
    }
}

