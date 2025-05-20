package com.mayu.common;

/**
 * @Author: 马瑜
 * @Date: 2023/9/6 16:13
 * @Description: 常量
 */
public class Constants {

    private Constants() {
    }

    public static final String DEFAULT_GROUP_NAME = "default";
    public static final String SBC = "SBC";
    public static final String MP3 = "MP3";
    public static final String PCM = "PCM";
    public static final String DOT = ".";
    public static final String SUFFIX_MP3 = ".mp3";
    public static final String SP = "/";

    public static final String MIRC_IF_COMPLAINT_STR = "微管家是否有处理投诉：";
    public static final String COMPLAINT_SATISFACTION_STR = "投诉处理满意度：";
    public static final String COMPLAINT_DISSATISFACTION_CAUSE_STR = "投诉处理不满意原因：";

    // 解析大模型返回结果(情绪识别)
    public static final String EMOTION_STATUS_STR = "情绪状态：";
    public static final String COMPLAINT_STATUS_STR = "投诉状态：";
    public static final String EMOTION_CAUSE_STR = "情绪原因：";

    public static final String NEGATIVE_ROLE_STR = "负面情绪方：";
    public static final String NEGATIVE_TYPE_STR = "负面情绪分类：";
}
