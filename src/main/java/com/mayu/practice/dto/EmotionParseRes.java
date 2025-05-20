package com.mayu.practice.dto;

import lombok.Data;

/**
 * @Author: 马瑜
 * @Date: 2024/7/9 17:18
 * @Description: 大模型返回的情绪检测结果 解析对象
 */
@Data
public class EmotionParseRes {

    // 情绪状态
    private String emotionStatus;

    // 投诉状态
    private String complaintStatus;

    // 情绪原因
    private String emotionCause;
}
