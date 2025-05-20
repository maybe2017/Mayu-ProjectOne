package com.mayu.practice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author: 马瑜
 * @Date: 2023/8/27 11:57
 * @Description: RabbitMQ生产者交换器、绑定、队列声明
 * 自动创建交换机 与 队列 及 绑定
 */
@Configuration
@Slf4j
public class MQQueueConfig {
    /**
     * 交换器
     */
    public static final String TEST_EXCHANGE_NAME = "test_exchange";
    /**
     * 队列
     */
    public static final String TEST_QUEUE_NAME = "test_queue";
    /**
     * 路由 test.routing.key
     */
    public static final String TEST_ROUTING_KEY = "routing_key_for_test_queue";

//    @Resource
//    private AmqpAdmin amqpAdmin;
//
//    @Bean
//    public DirectExchange createExchange() {
//        // 第一个参数为交换机名字，第二个参数为是否持久化，第三个参数为不使用交换机时删除
//        DirectExchange directExchange = new DirectExchange(TEST_EXCHANGE_NAME, true, false);
//        amqpAdmin.declareExchange(directExchange);
//        log.info("====>> 交换机创建成功! exchange:{}", TEST_EXCHANGE_NAME);
//        return directExchange;
//    }
//
//    @Bean
//    public Queue queue() {
//        /**
//         * 第一个参数为队列名字，
//         * 第二个参数为是否持久化，
//         * 第三个参数为是否排他（true：一个连接只能有一个队列，false：一个连接可以有多个（推荐））
//         * 第四个参数为不使用队列时自动删除
//         */
//        Queue queue = new Queue(TEST_QUEUE_NAME, true, false, false);
//        amqpAdmin.declareQueue(queue);
//        log.info("====>> 队列创建成功! queue:{}", TEST_QUEUE_NAME);
//        return queue;
//    }
//
//    @Bean
//    public Binding binding() {
//        /**
//         * 第一个参数为目的地，就是交换机或者队列的名字
//         * 第二个参数为目的地类型，交换机还是队列
//         * 第三个参数为交换机，QUEUE-队列  EXCHANGE-交换机
//         * 第四个参数为路由键，匹配的名称
//         */
//        Binding binding = BindingBuilder.bind(queue()).to(createExchange()).with(TEST_ROUTING_KEY);
//        amqpAdmin.declareBinding(binding);
//        log.info("====>> 绑定成功! exchange:{}, queue:{}, routing_key:{}", TEST_EXCHANGE_NAME, TEST_QUEUE_NAME, TEST_ROUTING_KEY);
//        return binding;
//    }
}
