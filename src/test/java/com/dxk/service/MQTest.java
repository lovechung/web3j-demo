package com.dxk.service;

import com.dxk.model.User;
import com.dxk.mq.Sender;
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
public class MQTest {

    @Resource
    private Sender sender;

    @Test
    public void testSend() {
        User user = new User(1L, "测试1", 28, LocalDate.now());
        sender.send(user);
    }
}
