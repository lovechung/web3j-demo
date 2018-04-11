package com.dxk.service;

import com.dxk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testSelectById() {
        User user = userService.selectById(1L);
        log.info("==========user：{}", user);
    }

    @Test
    public void testUpdate() {
        userService.update(new User(1L, "更新", 33, LocalDate.now()));
    }

    @Test
    public void testSave() {
        userService.save(new User(4L, "新增", 33, LocalDate.now()));
    }

    @Test
    public void testDelete() {
        userService.delete(1L);
    }
}
