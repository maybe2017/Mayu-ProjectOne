package com.mayu.practice.task;

import com.mayu.practice.config.EmailConfig;
import com.mayu.practice.po.DateRange;
import com.mayu.practice.service.AbstractMsgMonitorBase;
import com.mayu.practice.service.EmailService;
import com.mayu.practice.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 马瑜
 * @Date: 2023/6/21 10:12
 * @Description: 对上游消息推送动作中断状态的 监控任务
 */
@Slf4j
@Component
public class MsgPushSuspendMonitorTask extends AbstractMsgMonitorBase implements InitializingBean {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private EmailService emailService;

    @Resource
    private EmailConfig emailConfig;

    // 默认告警间隔 30分钟
    @Value("${message.monitor.alarmIntervalMinutes:30}")
    private int alarmIntervalMinutes;

    // 中断判定时间 10分钟
    @Value("${message.monitor.suspendThresholdMinutes:10}")
    private int suspendThresholdMinutes;

    @Value("${message.monitor.switch:false}")
    private boolean monitorSwitch;

    // 中断状态判定时间(秒数)
    private long suspendThresholdSeconds;

    // 告警间隔(秒数)
    private long alarmIntervalSeconds;

    @Override
    public void afterPropertiesSet() throws Exception {
        suspendThresholdSeconds = suspendThresholdMinutes * 60L;
        alarmIntervalSeconds = alarmIntervalMinutes * 60L;
    }

    @Scheduled(cron = "${message.monitor.cron}")
    public void marketCaseAnalysis() {

        if(!monitorSwitch){
            log.info("亚信消息推送动作中断监控定时任务开关-关闭");
            return;
        }
        try {
            log.info("亚信消息推送动作中断监控定时任务-开始");
            // 1. 判断是否在30分钟内已经告警过
            if (ifAlarmByEmailInInterval()) {
                return;
            }

            // 2. 判断是否在配置的时间范围
            if (!satisfyTimeRangeCondi()) {
                log.debug("当前时间不在配置的中断机制生效范围! 任务结束..");
                return;
            }

            // 3. 判断redis中存的最后一次消息推送时间，与当前时间比较，是否已经落后10分钟以上
            if (ifNoPushBeyondInterval()) {
                log.info("最后一次消息推送时间距离当前时间已超过了{}分钟, 尝试加锁进行告警!", suspendThresholdMinutes);
                boolean lockSuccess = false;
                try {
                    // 支持多节点，此处先获取锁，获取成功的节点进行告警
                    Boolean alarmLock = redisTemplate.opsForValue().setIfAbsent(ALARM_LOCK_KEY, DateUtils.getTime(),
                            ALARM_LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);
                    lockSuccess = null != alarmLock && alarmLock;
                    if (lockSuccess) {
                        log.debug("加告警锁成功! 有效时间:{}", ALARM_LOCK_EXPIRE_SECONDS);
                        if (emailService.sendEmail(emailConfig.getPushSuspendText())) {
                            redisTemplate.opsForValue().set(LAST_ALARM_TIME_KEY, DateUtils.getTime());
                        }
                    }
                } finally {
                    if (lockSuccess) {
                        log.debug("释放告警锁:{} !", ALARM_LOCK_KEY);
                        redisTemplate.delete(ALARM_LOCK_KEY);
                    }
                }
            }
            log.info("亚信消息推送动作中断监控定时任务-结束");
        } catch (Exception e) {
            log.error("亚信消息推送动作中断监控定时任务-异常{}", e.getMessage(), e);
        }
    }

    // 是否在Interval间隔分钟数内 已经告警过
    private boolean ifAlarmByEmailInInterval() {
        Object lastTimeOfEmailAlarmObj = redisTemplate.opsForValue().get(LAST_ALARM_TIME_KEY);
        if (null == lastTimeOfEmailAlarmObj) {
            // null表示可能还没设置 不进行告警
            return true;
        }
        return DateUtils.getDateDiffSec(new Date(), DateUtils.parseDate(lastTimeOfEmailAlarmObj)) <= alarmIntervalSeconds;
    }

    // 无消息推送是否已经超过了10分钟
    private boolean ifNoPushBeyondInterval() {
        Object lastTimeOfEmailAlarmObj = redisTemplate.opsForValue().get(LAST_PUSH_TIME_KEY);
        if (null == lastTimeOfEmailAlarmObj) {
            return false;
        }
        return DateUtils.getDateDiffSec(new Date(), DateUtils.parseDate(lastTimeOfEmailAlarmObj)) >= suspendThresholdSeconds;
    }

    // 是否在配置的生效时间范围之内
    private boolean satisfyTimeRangeCondi() {

        // 支持redis配置 且优先读redis
        log.debug("开始读取:中断判定机制的生效范围, 并进行有效判断(优先从redis读取)..");
        Object suspendValidTimeRange = redisTemplate.opsForValue().get(SUSPEND_VALID_TIME_RANGE);
        List<DateRange> dateRanges = null == suspendValidTimeRange ?
                emailConfig.getSuspendValidTimeRange() : emailConfig.doCheckAndGetTimeRange(suspendValidTimeRange.toString());

        if (!dateRanges.isEmpty()) {
            for (DateRange range : dateRanges) {
                if (DateUtils.belongTime(range.getLeftDate(), range.getRightDate(), new Date())) {
                    return true;
                }
            }
        }
        return false;
    }
}
