package com.dxk.service;

import com.dxk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 注解 @CacheConfig 是类级别的注解方式，用于将缓存分类，
 */
@CacheConfig(cacheNames = "user")
@Service
@Slf4j
public class UserService {

    /**
     * 注解 @Cacheable 用于查询操作
     * 1. 如果key存在，直接查询缓存中的数据
     * 2. 如果key不存在，查询db，并将结果更新到缓存中
     */
    @Cacheable(key = "'user_'.concat(#userId.toString())")
    public User selectById(Long userId) {
        return getData().get(userId);
    }

    /**
     * 注解 @CachePut 用于更新和插入操作，每次都会请求db
     * 1. 如果key存在，更新缓存/db
     * 2. 如果key不存在，插入缓存/db
     */
    @CachePut(key = "'user_'.concat(#user.id.toString())")
    public void update(User user) {
        getData().put(user.getId(), user);
    }

    @CachePut(key = "'user_'.concat(#user.id.toString())")
    public void save(User user) {
        getData().put(user.getId(), user);
    }

    /**
     * 注解 @CacheEvict 用于删除缓存中的数据，每次都会请求db
     */
    @CacheEvict(value = "user", key = "'user_'.concat(#userId.toString())")
    public void delete(Long userId) {
        getData().remove(userId);
    }

    private Map<Long, User> getData() {
        log.info("===============从数据源获取===============");
        Map<Long, User> map = new HashMap<>();
        map.put(1L, new User(1L, "测试1", 28));
        map.put(2L, new User(2L, "测试2", 18));
        map.put(3L, new User(3L, "测试3", 24));
        return map;
    }
}
