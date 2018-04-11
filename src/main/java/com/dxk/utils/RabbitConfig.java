package com.dxk.utils;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /**
     * 声明队列
     */
    @Bean
    public Queue queue1() {
        return new Queue("demo.queue1", true);
    }

    @Bean
    public Queue queue2() {
        return new Queue("demo.queue2", true);
    }

    /**
     * 声明交换机。
     * 生产者发送消息到交换机，交换机与队列绑定，消费者监听相应的队列来消费消息。
     * 模式：
     *   1.Fanout：发布订阅，交换机与每个队列的绑定方式是一对一
     *   2.Direct：完全路由，交换机与每个队列的绑定方式是根据路由键的完整匹配
     *   3.Topic：主题路由，交换机与每个队列的绑定方式是根据路由键的模糊匹配
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("demo.fanout");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("demo.direct");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("demo.topic");
    }

    /**
     * 声明队列与交换机的绑定关系
     */
    @Bean
    public Binding bindingFanoutExchange1(Queue queue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue1).to(fanoutExchange);
    }

    @Bean
    public Binding bindingFanoutExchange2(Queue queue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }

    @Bean
    public Binding bindingDirectExchange1(Queue queue1, DirectExchange directExchange) {
        return BindingBuilder.bind(queue1).to(directExchange).with("direct.msg");
    }

    @Bean
    public Binding bindingDirectExchange2(Queue queue2, DirectExchange directExchange) {
        return BindingBuilder.bind(queue2).to(directExchange).with("123");
    }

    @Bean
    public Binding bindingTopicExchange1(Queue queue1, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue1).to(topicExchange).with("topic.msg");
    }

    @Bean
    public Binding bindingTopicExchange2(Queue queue2, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue2).to(topicExchange).with("topic.msg.#");
    }
}
