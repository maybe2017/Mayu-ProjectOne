package com.mayu.practice.po.resourceInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  语义标签
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemanticLabel {

    /**
     * 标签标识
     */
    private String labelCode;
    /**
     * 标签名称
     */
    private String labelName;
    /**
     * 标签得分
     */
    private Double labelScore;

}
