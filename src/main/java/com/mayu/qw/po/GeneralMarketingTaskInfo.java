package com.mayu.qw.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "generalMarketingTaskInfo")
public class GeneralMarketingTaskInfo {

    /**
     * 通用营销任务id
     */
    private String taskId;

    /**
     * 营销话术(最长1000个字符)
     */
    private String wordsTechnique;
    /**
     * 任务开始时间(yyyy-MM-dd HH:mm:ss)
     */
    private String startTime;
    /**
     * 任务结束时间(yyyy-MM-dd HH:mm:ss)
     */
    private String endTime;
    /**
     * 下线时间(yyyy-MM-dd HH:mm:ss)
     */
    private String offlineTime;
    /**
     * 任务状态:2-进行中;4-已下线
     */
    private String taskStatus;
    /**
     * 微管家userid列表
     */
    private List<GeneralMarketingUserRequest> userList;

    /**
     * 话术关键词列表
     */
    private List<GeneralMarketingKeyWordRequest> keyWordList;

    /**
     * 分析标志(0：未分析，1：已分析)
     */
    private String analysisFlg;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 商品主动营销大类 只有第一次推送才有
     */
    private String marketBusiTypeName;
    /**
     * 商品主动营销小类 只有第一次推送才有
     */
    private String marketSubBusiTypeName;
}
