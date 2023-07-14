package com.mayu.practice.service;

import com.mayu.practice.utils.DateUtils;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

/**
 * @Author: 马瑜
 * @Date: 2023/6/26 15:49
 * @Description: 对上游消息推送的监控服务
 */
@Data
public abstract class AbstractMsgMonitorBase {
    protected AbstractMsgMonitorBase(){}

    // 上游最后一次消息的推送时间
    protected static final String LAST_PUSH_TIME_KEY = "msg_monitor:LastTimeOfRecordPush";

    // 最后一次邮件的告警时间
    protected static final String LAST_ALARM_TIME_KEY = "msg_monitor:LastTimeOfEmailAlarm";

    // 延迟计数器
    protected static final String DELAY_PUSH_COUNT_KEY = "msg_monitor:CountOfDelayPush";

    // 延迟消息id集(抽样)
    protected static final String DELAY_MSG_IDS_KEY = "msg_monitor:MsgIdsOfDelayPush";

    // 告警锁
    protected static final String ALARM_LOCK_KEY = "msg_monitor:LockOfAlarm";

    // 告警锁有效时间(1分钟)
    protected static final long ALARM_LOCK_EXPIRE_SECONDS = 60;

    // 中断判定机制的生效范围(24小时制, 多个区间以逗号分割), redis中配置key
    protected static final String SUSPEND_VALID_TIME_RANGE = "msg_monitor:SuspendValidTimeRange";

    // 是否在Interval间隔分钟数内 已经告警过； true表示已经告警过
    protected boolean ifAlarmByEmailInInterval(RedisTemplate<String, Object> redisTemplate, long alarmIntervalSeconds) {
        Object lastTimeOfEmailAlarmObj = redisTemplate.opsForValue().get(LAST_ALARM_TIME_KEY);
        if (null == lastTimeOfEmailAlarmObj) {
            // null表示可能第一次还没设置 需要继续向后走判断能否告警的逻辑
            return false;
        }
        return DateUtils.getDateDiffSec(new Date(), DateUtils.parseDate(lastTimeOfEmailAlarmObj)) <= alarmIntervalSeconds;
    }

}
