package com.mayu.qw.po.resp;

import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * @Author: 马瑜
 * @Date: 2024/10/15 16:48
 * @Description: 管家营销话术校验结果
 */
@Data
public class CheckMarketingRhetoricRes {

    // 流水号
    private String seqNo;

    // 审核时间
    private String checkTimeStr;

    // 审核结果: 有无错别字
    private Boolean hasTypoWords;

    // 审核结果: 错别字集合
    private List<String> typoWordsList;

    // 审核结果: 有无歧义
    private Boolean hasAmbiguityWords;

    // 审核结果: 歧义集合
    private List<String> ambiguityWordsList;

    // 审核结果: 有无异常符号
    private Boolean hasExceptionWords;

    // 审核结果: 异常符号集合
    private List<String> exceptionWordsList;
}
