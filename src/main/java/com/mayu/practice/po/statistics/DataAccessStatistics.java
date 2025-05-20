package com.mayu.practice.po.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: 马瑜
 * @Date: 2024/5/28 14:04
 * @Description: 数据接入的统计, 用定时任务聚合来更新当前的统计数据，延迟取决于定时任务的执行间隔
 * 唯一索引：接入数据时间 + 接入平台
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dataAccessStatistics")
public class DataAccessStatistics {

    private String id;

    // 接入数据时间 yyyy-MM-dd
    private String parseDataDate;

    // 接入平台 ## AccessSysType
    private String accessSys;

    // 当日已接入总量(所有的，合法与不合法)
    private int accessDataCount;

    // 当日因过滤规则 判定为非法而不提交质检的数据量
    private int filterDataCount;

    // 当日发起质检的数据总量(调用质检接口成功)
    private int trySubmitCount;

    // 因产生业务运行异常导致未提交、或提交质检失败的数量
    private int bizExceptionCount;

    // 质检流程成功数量
    private int qualitySuccCount;

}
