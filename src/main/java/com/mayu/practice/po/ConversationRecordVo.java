package com.mayu.practice.po;

import lombok.Data;

/**
 * @Author: 马瑜
 * @Date: 2023/6/25 16:01
 * @Description: 模拟亚信侧推送的消息
 */
@Data
public class ConversationRecordVo {

    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 消息时间
     */
    private String msgTime;
}
