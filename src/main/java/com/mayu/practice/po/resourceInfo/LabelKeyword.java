package com.mayu.practice.po.resourceInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabelKeyword {
    /**
     * @see com.iflytek.sas.common.po.mongo.NodeAttribute.Type
     * 表示检测类型：语速、静音、内容等等
     */
    private String type;
    /**
     * 匹配到的值
     */
    private String value;
    /**
     * 标准值
     */
    private String standard;
    /**
     * 匹配节点
     */
    private int partId;
    /**
     * 内容匹配label定位(字符串开始位置)
     */
    private int index;
    /*
     *  匹配类型1-正则2-语义3-冷启动
     */
    private Set<String> matchType;
    /**
     *  语义标签集合
     */
    private List<SemanticLabel> semanticLabelList;
}

