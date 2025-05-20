package com.mayu.qw.service;

import com.alibaba.fastjson.JSON;
import com.mayu.practice.base.ResponseMap;
import com.mayu.qw.po.ConversationRecord;
import com.mayu.qw.po.GeneralMarketingKeyWordRequest;
import com.mayu.qw.po.GeneralMarketingTaskInfo;
import com.mayu.qw.repository.ConversationRecordRepository;
import com.mayu.qw.repository.GeneralMarketingTaskInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 马瑜
 * @Date: 2024/10/11 15:52
 * @Description:
 */
@Slf4j
@Service
public class CheckMircoTaskService {

    @Resource
    private ConversationRecordRepository conversationRecordRepository;
    @Resource
    private GeneralMarketingTaskInfoRepository generalMarketingTaskInfoRepository;

    public String testGeneralMarketingReplyInfo(String taskId) {
        log.info("w管家营销任务[{}]匹配 -> 尝试生成GeneralMarketingReplyInfo !!!", taskId);
        GeneralMarketingTaskInfo taskInfo = generalMarketingTaskInfoRepository.findByTaskId(taskId);
        if (null == taskInfo) {
            throw new RuntimeException("营销任务查询无数据");
        }
        List<ConversationRecord> records = conversationRecordRepository.listMicroConversationRecordByMsgTime(taskInfo.getStartTime(), taskInfo.getEndTime());
        log.info("w管家在营销任务[{}]时间内[{} - {}]的消息数: {}", taskId, taskInfo.getStartTime(), taskInfo.getEndTime(), records.size());
        List<String> hitMsgIds = new ArrayList<>();
        for (ConversationRecord record : records) {
            if (checkValidAndMatchWords(record, taskInfo)) {
                hitMsgIds.add(record.getMsgId());
            }
        }
        return JSON.toJSONString(hitMsgIds);
    }


    /**
     * 先验证管家消息时间 是否在 营销任务时间范围内，再尝试匹配营销任务话术 与 管家发送的消息内容
     *
     * @param record 微管家发送的消息
     * @param info   营销任务
     * @return 匹配上了关键词 返回true
     */
    private boolean checkValidAndMatchWords(ConversationRecord record, GeneralMarketingTaskInfo info) {
        boolean matchSuccess = false;

        String msgContent = record.getMsgContent();
        String wordsTechnique = info.getWordsTechnique();

        // 会话详单时间，不在营销有效期内
        if (!(record.getMsgTime().compareTo(info.getStartTime()) >= 0 && record.getMsgTime().compareTo(info.getEndTime()) <= 0)) {
            log.info("w管家发送消息的时间，不在营销任务有效期内，消息id：{}，任务id:{}", record.getMsgId(), info.getTaskId());
            return matchSuccess;
        }
        // 会话详单微管家ID，不在营销微管家Id集合内
        if (!(CollectionUtils.isNotEmpty(info.getUserList()) && CollectionUtils.isNotEmpty(info.getUserList().stream()
                .filter(f -> f.getUserId().equals(record.getSendUser())).collect(Collectors.toList())))) {
            log.info("w管家id，不在当前营销任务id集合内，消息id：{}，任务id:{}", record.getMsgId(), info.getTaskId());
            return matchSuccess;
        }
        // 空判断
        if (StringUtils.isBlank(msgContent)
                || (StringUtils.isBlank(wordsTechnique) && CollectionUtils.isEmpty(info.getKeyWordList()))
                || StringUtils.isBlank(record.getMsgTime())
                || StringUtils.isBlank(info.getStartTime())
                || StringUtils.isBlank(info.getEndTime())) {
            log.info("w管家消息体|消息时间|营销任务话术｜任务开始、结束时间存在空数据，消息id：{}，任务id:{}", record.getMsgId(), info.getTaskId());
            return matchSuccess;
        }
        // 会话内容不包含营销话术 或者 会话内容里面不包含关键字
        if (!msgContent.contains(wordsTechnique) && !judgeMsgContainKey(msgContent, info.getKeyWordList())) {
            log.info("w管家发送消息体内容不包含营销话术 或者 消息体内容里面不包含全部的关键字! 消息id：{}，任务id:{}", record.getMsgId(), info.getTaskId());
            return matchSuccess;
        }
        log.info("w管家发送消息体内容匹配成功! 消息id：{}，任务id:{}", record.getMsgId(), info.getTaskId());
        return true;
    }

    // 判断管家消息内容是不是包含所有有关键字
    // 是则返回true
    private boolean judgeMsgContainKey(String msgContent, List<GeneralMarketingKeyWordRequest> keyWordList) {
        if (CollectionUtils.isEmpty(keyWordList)) {
            return false;
        }
        int size = keyWordList.size();
        int count = 0;
        for (GeneralMarketingKeyWordRequest vo : keyWordList) {
            if (msgContent.contains(vo.getKeyWord())) {
                count++;
            }
        }
        if (size == count) {
            return true;
        }
        return false;
    }
}
