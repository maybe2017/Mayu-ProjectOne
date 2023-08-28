package com.mayu.practice.service.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class MessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(String queueName, String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("消息[`{}`], 发送时间: {}", msg, sdf.format(new Date()));
        rabbitTemplate.convertAndSend("test_delay_exchange", queueName, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 注意在发送的时候，必须加上一个header, 表示设置的延迟时间是30秒
                message.getMessageProperties().setHeader("x-delay", 30000);
                return message;
            }
        });
    }
}