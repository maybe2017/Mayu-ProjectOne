package com.mayu.practice.service;

import com.mayu.practice.po.ConversationRecordVo;

import java.util.List;

/**
 * @Author: 马瑜
 * @Date: 2023/6/19 09:52
 * @Description: 对上游消息推送动作 监控服务
 */
public interface MessagePushMonitorService {

    /**
     * 消息过滤
     */
    void handleMessage(List<ConversationRecordVo> conversationList);

    /**
     * 告警处理
     */
    boolean alarm();

}
