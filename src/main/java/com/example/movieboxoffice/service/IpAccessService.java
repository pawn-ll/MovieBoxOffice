package com.example.movieboxoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IpAccessService {

    @Autowired
    private RedisService redisService;

    private static final String IP_ACCESS_KEY_FORMAT = "ip_access_%s";

    public void incrementAccessCount(String ipAddress) {
        String key = String.format(IP_ACCESS_KEY_FORMAT, ipAddress);
        Integer count =  (Integer)redisService.get(key);
        if (count == null) {
            count = 0;
        }
        count++;
        redisService.set(key, count);
        // 设置过期时间，例如1小时，防止旧的访问记录占用过多内存
        redisService.expire(key, 60 * 60);
    }


    public boolean isAccessRestricted(String ipAddress) {

        String key = String.format(IP_ACCESS_KEY_FORMAT, ipAddress);
        Integer count = (Integer) redisService.get(key);
        return count != null && count >= 2000;
    }

}

