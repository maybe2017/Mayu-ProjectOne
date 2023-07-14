package com.mayu.practice.po.req;

import lombok.Data;

/**
 * @Author: 马瑜
 * @Date: 2023/6/26 15:24
 * @Description: 消息推送监控-中断监控-监控时间区间配置
 */
@Data
public class UpdateSuspendValidTimeRangeReq {
    private String timeRange;
}
