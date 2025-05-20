//package com.mayu.practice.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.mayu.practice.base.ResponseMap;
//import com.mayu.practice.config.MQQueueConfig;
//import com.mayu.practice.po.ConversationRecordVo;
//import com.mayu.practice.service.rabbitmq.MessageProducer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
///**
// * @Author: 马瑜
// * @Date: 2023/6/25 16:10
// * @Description: MQ
// */
//@Slf4j
//@RestController
//@RequestMapping("/rabbit")
//public class MQController {
//
//    @Resource
//    private MessageProducer producer;
//
//    @PostMapping(value = "/send")
//    public ResponseMap send(@RequestBody ConversationRecordVo recordVo) {
//        log.info("接收到消息! {}", JSON.toJSONString(recordVo));
//        producer.sendMsg(MQQueueConfig.TEST_QUEUE_NAME, JSON.toJSONString(recordVo));
//        return ResponseMap.success();
//    }
//}
