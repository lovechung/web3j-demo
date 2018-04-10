package com.dxk.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Receiver {

    @RabbitListener(queues = "demo.queue1")
    public void processMsg1(String msg) {
        log.info("队列[queue1]接收了一条消息，内容为：{}", msg);
    }

    @RabbitListener(queues = "demo.queue2")
    public void processMsg2(String msg) {
        log.info("队列[queue2]接收了一条消息，内容为：{}", msg);
    }
}