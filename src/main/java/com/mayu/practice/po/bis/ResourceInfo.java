package com.mayu.practice.po.bis;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ShuaiWang on 2017/9/18.
 */

@Data
@Document(collection = "resourceInfo")
public class ResourceInfo {
    private String id;
    /**
     * 输入ID，生成规则暂不定义。创建唯一索引
     */
    private String taskId;
    /**
     * 创建时间UTC
     */
    private LocalDateTime createTime;
    /**
     * 输入类型，定义类型为：VOICE_V1(音频类型，版本：1，简写为V1；短信类型SMS_V1，简写为S1)
     */
    private String type;
    /**
     * 记录已完成的操作
     */
    private String operatorName;
    /**
     * 统计状态，由统计工具更新。0、正在统计；1、统计完成
     */
    private String statisticsStatus;
    /**
     * 基础信息
     */
    private ResourceAttr attr;
    /**
     * 转写引擎转写结果
     */
    private Text text;
    /**
     * 标签
     */
    private List<ResourceLabel> label;

    /**
     * 三方参数
     */
    private ThirdParam thirdParams;

    private String channel;
}
