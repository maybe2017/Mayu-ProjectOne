package com.mayu.practice.po.bis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ShuaiWang on 2017/9/19
 * @author lazycece on 2019/04/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceLabel {
    private String ruleId;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 规则类别
     */
    private String ruleType;
    /**
     * 是否是命中规则
     */
    private Boolean hit;
    /**
     * 规则标签
     */
    private Integer flag;
    /**
     * 匹配的规则版本
     */
    private Integer version;
}
