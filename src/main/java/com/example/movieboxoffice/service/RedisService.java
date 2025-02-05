package com.example.movieboxoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    // 设置键值对
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    // 获取键对应的值
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    // 批量获取键的值
    public List<Object> mget(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }
    // 删除键
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 判断键是否存在
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    // 设置过期时间，单位为秒
    public void expire(String key, long seconds) {
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    // 获取键的过期时间，返回值为剩余生存时间，单位为毫秒
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    // 使用哈希操作
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public void hDelete(String key, String... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    // 使用列表操作
    public void lPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    // 使用集合操作
    public void sAdd(String key, Object member) {
        redisTemplate.opsForSet().add(key, member);
    }

    public boolean sIsMember(String key, Object member) {
        return redisTemplate.opsForSet().isMember(key, member);
    }

    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }
    // 使用有序集合操作
    public void zAdd(String key, double score, Object member) {
        redisTemplate.opsForZSet().add(key, member, score);
    }

    public Long zRank(String key, Object member) {
        return redisTemplate.opsForZSet().rank(key, member);
    }

    // 清空整个Redis
    public void flushDB() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
