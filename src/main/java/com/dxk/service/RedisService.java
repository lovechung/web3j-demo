package com.dxk.service;

import com.dxk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class RedisService {

    @Resource
    private RedisTemplate redisTemplate;

    public void setStr(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getStr(String key) {
        return redisTemplate.opsForValue().get(key).toString();
    }

    public void setList(String key, List list) {
        redisTemplate.opsForList().leftPush(key, list);
//        redisTemplate.opsForList().rightPush(key, list);
    }

    public List getList(String key) {
        return (List) redisTemplate.opsForList().leftPop(key);
    }

    public void setSet(String key, Set set) {
        redisTemplate.opsForSet().add(key, set);
    }

    public Set getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public void setHash(String key, Map map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Map getHash(String key) {
        Set<String> keys = redisTemplate.opsForHash().keys(key);
        log.info("==========keys：{}", keys);
        List<String> values = redisTemplate.opsForHash().values(key);
        log.info("==========values：{}", values);
        return redisTemplate.opsForHash().entries(key);
    }
}
