package com.mayu.practice.service;

import com.alibaba.fastjson.JSON;
import com.mayu.practice.config.EmailConfig;
import com.mayu.practice.po.ConversationRecordVo;
import com.mayu.practice.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 马瑜
 * @Date: 2023/6/19 09:53
 * @Description: 对上游消息推送动作的监控, 推送动作中若存在消息延迟大于约定分钟数，以推送动作为粒度，如果超过10次，以126邮箱告警
 */
@Service
@Slf4j
public class MessagePushMonitorServiceImpl extends AbstractMsgMonitorBase implements MessagePushMonitorService, InitializingBean {

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

    // 默认最大延迟 30分钟
    @Value("${message.monitor.delayThresholdMinutes:30}")
    private int delayThresholdMinutes;

    // 告警条件: 10次推送延迟
    @Value("${message.monitor.alarmIfDelayCount:10}")
    private int alarmIfDelayCount;

    // 延迟的判定时间(秒数)
    private long delayThresholdSeconds;

    // 告警间隔(秒数)
    private long alarmIntervalSeconds;

    // 最大历史计数
    private int historyDelayMaxCount;

    // redis lua脚本: 维护一个定长队列(长度10)，每次新数据加到队列头部，超过长度部分丢弃
    private static final String FIXED_LENGTH_QUEUE_LUA_SCRIPT =
            "local key = KEYS[1] " +
                    "redis.call('lpush',key,unpack(ARGV))" +
                    "redis.call('ltrim',key,0,9)";

    @Override
    public void afterPropertiesSet() throws Exception {
        delayThresholdSeconds = delayThresholdMinutes * 60L;
        alarmIntervalSeconds = alarmIntervalMinutes * 60L;
        historyDelayMaxCount = alarmIfDelayCount - 1;
    }

    @Override
    public void handleMessage(List<ConversationRecordVo> conversationList) {
        try {
            doHandleMessage(conversationList);
        } catch (Exception e) {
            log.error("消息推送监控服务异常! {}", e.getMessage(), e);
        }
    }

    private void doHandleMessage(List<ConversationRecordVo> conversationList) {
        if (CollectionUtils.isEmpty(conversationList)) {
            log.warn("本次推送无消息!");
            return;
        }

        // 1. 将本次推送时间记录，刷新redis中key为'RecordLastPushTime'(最近一次推送时间) 的值。
        redisTemplate.opsForValue().set(LAST_PUSH_TIME_KEY, DateUtils.getTime());

        // 2. 判断是否在30分钟内已经告警过
        if (ifAlarmByEmailInInterval(redisTemplate, alarmIntervalSeconds)) {
            return;
        }

        // 3. 遍历本次推送的消息集, 若存在延迟超过30分钟的, 以本次推送动作为粒度 记入redis计数器。
        refreshCacheIfExitsDelayRecord(conversationList);
    }

    // 若存在消息推送延迟时间大于某个值, 记录延迟的消息数量
    public void refreshCacheIfExitsDelayRecord(List<ConversationRecordVo> conversationList) {
        boolean noDelay = true;
        for (ConversationRecordVo recordVo : conversationList) {
            String msgTime = recordVo.getMsgTime();
            Date currentDate = new Date();
            if (StringUtils.isBlank(msgTime)) {
                continue;
            }
            long diffSec = DateUtils.getDateDiffSec(currentDate, DateUtils.parseDate(msgTime));
            if (diffSec >= delayThresholdSeconds) {
                String msgId = recordVo.getMsgId();
                log.info("本次上游推送消息集中, 存在延迟时间超过{}分钟的消息! msgId:{}", delayThresholdMinutes, msgId);
                Object countOfDelayPushObj = redisTemplate.opsForValue().get(DELAY_PUSH_COUNT_KEY);
                log.debug("获取到已延迟次数'ms_monitor_CountOfDelayPush'值: {}", countOfDelayPushObj);
                if (null != countOfDelayPushObj) {
                    int countOfDelayPush = Integer.parseInt(countOfDelayPushObj.toString());
                    // 如果当前历史计数已经是9，那么本次就应该告警
                    if (countOfDelayPush >= historyDelayMaxCount) {
                        handleAlarmTryLock();
                    } else {
                        // 累加, 此处需对返回值处理
                        int countAfterIncr = setAndReturnCountKey(false, msgId);
                        if (countAfterIncr >= alarmIfDelayCount) {
                            handleAlarmTryLock();
                        }
                    }
                } else {
                    // 设置计数器 此处不再考虑处理返回值大于最大计数情况，告警皆由判断阶段处理..
                    setAndReturnCountKey(true, msgId);
                }
                noDelay = false;
                break;
            }
        }

        // 本次推送没有延迟消息，则清空redis计数器
        if (noDelay) {
            if (Objects.equals(redisTemplate.hasKey(DELAY_PUSH_COUNT_KEY), Boolean.TRUE)) {
                log.info("本次推送没有延迟消息，清空redis计数器!");
                deleteByKeys(Arrays.asList(DELAY_PUSH_COUNT_KEY, DELAY_MSG_IDS_KEY));
            }
        }

    }

    // 获取锁失败，则可能是其他节点已经进行告警，当前节点返回即可
    public void handleAlarmTryLock() {
        log.info("延迟推送次数已达到阈值:{}次, 尝试加锁进行告警!", alarmIfDelayCount);
        boolean lockSuccess = false;
        try {
            // 支持多节点，此处先获取锁，获取成功的节点进行告警
            // 考虑过期时间
            Boolean alarmLock = redisTemplate.opsForValue().setIfAbsent(ALARM_LOCK_KEY, DateUtils.getTime(),
                    ALARM_LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);
            lockSuccess = null != alarmLock && alarmLock;
            if (lockSuccess) {
                log.info("加告警锁成功! 有效时间:{}s", ALARM_LOCK_EXPIRE_SECONDS);
                if (alarm()) {
                    redisTemplate.opsForValue().set(LAST_ALARM_TIME_KEY, DateUtils.getTime());
                    deleteByKeys(Arrays.asList(DELAY_PUSH_COUNT_KEY, DELAY_MSG_IDS_KEY));
                }
            }
        } finally {
            if (lockSuccess) {
                deleteByKeys(Collections.singletonList(ALARM_LOCK_KEY));
            }
        }
    }


    @Override
    public boolean alarm() {
        return doAlarm();
    }

    // 同步处理
    private boolean doAlarm() {
        List<Object> delayMsgIdsObj = redisTemplate.opsForList().range(DELAY_MSG_IDS_KEY, 0, -1);
        String txt = String.format(emailConfig.getDelayText(), JSON.toJSONString(delayMsgIdsObj));
        return emailService.sendEmail(txt);
    }


    // 设置计数器并 返回设置后的值
    private int setAndReturnCountKey(boolean setNew, String msgId) {

        redisTemplate.opsForValue().increment(DELAY_PUSH_COUNT_KEY);
        if (setNew) {
            // 这里不考虑多节点
            return 1;
        }

        // 此处需要考虑一种情形: 假如一直判定为延迟且 邮件一直发送失败, 那么此处会向redis一直写消息id ==>> 需保证队列最多十个元素
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(FIXED_LENGTH_QUEUE_LUA_SCRIPT);
        redisTemplate.execute(redisScript, Collections.singletonList(DELAY_MSG_IDS_KEY), msgId);

        Object countAfterIncr = redisTemplate.opsForValue().get(DELAY_PUSH_COUNT_KEY);
        return countAfterIncr == null ? 0 : (Integer) countAfterIncr;
    }


    // 删除key
    public void deleteByKeys(List<String> keys) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            if (CollectionUtils.isNotEmpty(keys)) {
                for (String key : keys) {
                    connection.del(key.getBytes());
                }
            }
            return null;
        });
    }
}
