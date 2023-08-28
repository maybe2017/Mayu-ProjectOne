package com.mayu.practice;

import com.mayu.practice.service.rabbitmq.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: 马瑜
 * @Date: 2023/8/27 12:11
 * @Description: rabbitMQ测试
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeptuneApplication.class)
public class RabbitmqTest {
    @Resource
    private MessageProducer producer;

    @Test
    public void send() {
        producer.sendMsg("delay_queue_for_test","hello i am delay msg with 30 seconds delay");
    }
}
