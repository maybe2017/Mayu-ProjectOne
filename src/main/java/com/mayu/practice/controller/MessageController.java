package com.mayu.practice.controller;

import com.google.common.base.Joiner;
import com.mayu.practice.base.ResponseMap;
import com.mayu.practice.config.EmailConfig;
import com.mayu.practice.po.ConversationRecordVo;
import com.mayu.practice.po.DateRange;
import com.mayu.practice.po.req.UpdateSuspendValidTimeRangeReq;
import com.mayu.practice.service.MessagePushMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 马瑜
 * @Date: 2023/6/25 16:10
 * @Description: 消息
 */
@Slf4j
@RestController
@RequestMapping("/conversation")
public class MessageController {

    // 中断判定机制的生效范围(24小时制, 多个区间以逗号分割), redis中配置key
    private static final String SUSPEND_VALID_TIME_RANGE = "msg_monitor:SuspendValidTimeRange";

    @Resource
    private EmailConfig emailConfig;

    @Resource
    private MessagePushMonitorService messagePushMonitorService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping(value = "/synchronization")
    public ResponseMap dataSynchronization(@RequestBody List<ConversationRecordVo> conversationList) {
        log.info("接收到推送消息! 本次条数:{}", conversationList.size());
        // 告警
        messagePushMonitorService.handleMessage(conversationList);
        return ResponseMap.success();
    }

    @PostMapping(value = "/msgMonitor/suspendValidTimeRange/update")
    public ResponseMap updateConfig(@RequestBody UpdateSuspendValidTimeRangeReq req) {
        String validStr = "参数错误! 请按配置规范进行配置: 24小时制, 多个区间以逗号分割!";
        if (StringUtils.isNotBlank(req.getTimeRange())) {
            List<DateRange> validRangeList = emailConfig.doCheckAndGetTimeRange(req.getTimeRange().trim());
            if (CollectionUtils.isNotEmpty(validRangeList)) {
                List<String> validRangeStrL = new ArrayList<>();
                for (DateRange range : validRangeList) {
                    String currRange = range.getLeftDate() + "-" + range.getRightDate();
                    validRangeStrL.add(currRange);
                }
                validStr = Joiner.on(",").join(validRangeStrL);
                redisTemplate.opsForValue().set(SUSPEND_VALID_TIME_RANGE, validStr);
            }
            return ResponseMap.success(validStr);
        }
        return ResponseMap.fail(validStr);
    }
}
