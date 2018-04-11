package com.dxk.mq;

import com.dxk.model.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Sender {

    @Value("${mq.exchange.name}")
    private String exchange;
    @Value("${mq.routing-key}")
    private String routingKey;

    @Resource
    private AmqpTemplate rabbitTemplate;

    public void send(User user) {
        rabbitTemplate.convertAndSend(exchange, routingKey, user);
    }
}
