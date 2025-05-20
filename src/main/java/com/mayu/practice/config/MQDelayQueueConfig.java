package com.mayu.practice.config;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 马瑜
 * @Date: 2023/8/27 11:57
 * @Description: RabbitMQ生产者交换器、绑定、队列声明
 * 这里的延迟意味着 消息延迟路由到 队列或其它交换器
 */
@Configuration
public class MQDelayQueueConfig {

    /**
     * 延迟交换器
     */
    public static final String DELAY_TEST_EXCHANGE = "test_delay_exchange";
    /**
     * 延迟队列
     */
    public static final String DELAY_TEST_QUEUE = "delay_queue_for_test";
    /**
     * 延迟路由 delay.test.routing.key
     */
    public static final String DELAY_TEST_ROUTING_KEY = "delay_queue_for_test";

//
//    @Bean
//    public CustomExchange delayExchange() {
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        // 特别注意: 使用的是CustomExchange, 不是DirectExchange，另外CustomExchange的类型必须是x-delayed-message
//        return new CustomExchange(DELAY_TEST_EXCHANGE, "x-delayed-message", true, false, args);
//    }
//
//    @Bean
//    public Queue delayQueue() {
//        // 定义优先级队列，消息最大优先级为15，优先级范围为0-15，数字越大优先级越高
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-max-priority", 15);
//        // 设置持久化队列
//        return new Queue(DELAY_TEST_QUEUE, true);
//    }
//
//    @Bean
//    public Binding delayBinding() {
//        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(DELAY_TEST_ROUTING_KEY).noargs();
//    }
}
