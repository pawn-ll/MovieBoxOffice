package com.example.movieboxoffice.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Value("${sms.product}")
    private String product ;
    @Value("${sms.domain}")
    private String domain ;
    @Value("${sms.accessKeyId}")
    private String accessKeyId ;
    @Value("${sms.accessKeySecret}")
    private String accessKeySecret ;

    @Bean
    public IAcsClient acsClient() {
        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", product, domain);
        return new DefaultAcsClient(profile);
    }
}
