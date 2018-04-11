package com.dxk.mq;

import com.dxk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Receiver {

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(value = "${mq.queue}", durable = "true"),
            exchange = @Exchange(type = "${mq.exchange.type}", name = "${mq.exchange.name}"),
            key = ""
    )})
    public void process(User user) {
        log.info("队列[queue1]接收了一条消息，内容为：{}", user);
    }

}