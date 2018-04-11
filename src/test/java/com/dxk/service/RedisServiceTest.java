package com.dxk.service;

import com.dxk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisServiceTest {

    @Resource
    private RedisService redisService;

    @Test
    public void testSetStr() {
        redisService.setStr("demo", "hello world");
    }

    @Test
    public void testGetStr() {
        String value = redisService.getStr("demo");
        log.info("=====================demo:{}", value);
    }

    @Test
    public void testSetList() {
        List<String> list = Arrays.asList("a1", "a2", "a3");
        List<User> users = Arrays.asList(
                new User(1L, "测试1", 28, LocalDate.now()),
                new User(2L, "测试2", 18, LocalDate.parse("2014-02-28")),
                new User(3L, "测试3", 24, LocalDate.of(1991, 12, 12)));
        redisService.setList("users", users);
//        redisService.setList("list", list);
    }

    @Test
    public void testGetList() {
//        List<String> list = redisService.getList("list");
        List<User> users = redisService.getList("users");
        log.info("=====================users:{}", users);
    }

    @Test
    public void testSetSet() {
        Set<String> set = new HashSet<>();
        set.add("aaa");
        set.add("bbb");
        set.add("ccc");
        redisService.setSet("set", set);
    }

    @Test
    public void testGetSet() {
        Set<String> set = redisService.getSet("set");
        log.info("=====================set:{}", set);
    }

    @Test
    public void testSetHash() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        redisService.setHash("map", map);
    }

    @Test
    public void testGetHash() {
        Map<String, String> map = redisService.getHash("map");
        log.info("=====================map:{}", map);
    }
}
