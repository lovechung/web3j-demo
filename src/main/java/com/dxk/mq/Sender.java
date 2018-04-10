package com.dxk.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Sender {

    @Resource
    private AmqpTemplate rabbitTemplate;

    public void send(String exchange, String routingKey) {
        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                "这是一条来自交换机[" + exchange + "]" + "和路由键<" + routingKey + ">的测试消息");
    }
}
