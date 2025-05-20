package com.mayu.qw.po.req;

import lombok.Data;

/**
 * @Author: 马瑜
 * @Date: 2024/10/15 16:41
 * @Description: 管家营销话术校验请求类
 */
@Data
public class CheckMarketingRhetoricReq {

    // 营销话术类型(1:通用营销任务话术, 2:通用营销任务话术, 3:通用营销任务话术)
    private Integer type;

    // 流水号
    private String seqNo;
    
    // 营销话术内容
    private String content;

}
