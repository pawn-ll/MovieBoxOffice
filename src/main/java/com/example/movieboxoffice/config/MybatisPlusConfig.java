package com.example.movieboxoffice.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置MP的分页拦截器
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor=new MybatisPlusInterceptor();  //创建一个MP拦截器
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor()); //向MP拦截器添加分页拦截器
        return mybatisPlusInterceptor;
    }
}

