package com.mayu.practice.po.bis;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * Created by ShuaiWang on 2017/9/18.
 * 转写引擎结果
 */
@Data
public class Text {
    /**
     * 转写引擎原始输出json
     */
    private String engineOut;
    /**
     * 如果经过sas-text-proces组件，则用词字段保留文本过滤之前的格式化全文本
     */
    private String originWholeFormatOut;
    /**
     * 全文本格式化
     */
    private String wholeFormatOut;
    /**
     * 客服全文本格式化
     */
    private String csFormatOut;
    /**
     * 用户全文本格式化
     */
    private String userFormatOut;
    /**
     * 虚拟首节点
     */
    private InventedNode inventedNode;
    /**
     * 客服角色（根据关键字识别，其它角色为用户
     */
    private String csRole;
    /**
     * 通话总时长（单位毫秒）
     */
    private Integer totalTime;
    /**
     * 有效时间  （单位毫秒）
     */
    private Integer validTime;

    /**
     * 分词词性列表
     */
    private Map<String, Set<String>> wordFlag;
}
