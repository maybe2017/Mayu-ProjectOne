package com.mayu.practice.po.resourceInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc: 评分相关
 * @author  hjzhang21
 * @datetime  2022/6/10 15:16
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabelScore {
    /**
     * 关联评分0-不关联1-关联
     */
    private String associatedScore;
    /**
     * 命中得分
     */
    private Integer hitScore;
    /**
     * 未命中得分
     */
    private Integer missHitScore;
}
