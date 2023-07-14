package com.mayu.practice.po.bis;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by ShuaiWang on 11/4/2018.
 */
public class StateStats {

    @Id
    Integer id;

    @Field
    int callType;

    @Field
    String channel;

    /**
     * 通话量
     */
    @Field("callTotal")
    Long callTotal;

    /**
     * 通话时长（单位毫秒）
     */
    @Field("totalTime")
    Long totalTime;
    /**
     * 无效时长（单位毫秒）
     */
    @Field("invalidAudioDuration")
    Long invalidAudioDuration;
    /**
     * 最大通话时长（单位毫秒）
     */
    @Field("maxTime")
    Long maxTime;
    /**
     * 最小通话时长（单位毫秒）
     */
    @Field("minTime")
    Long minTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getCallTotal() {
        return callTotal;
    }

    public void setCallTotal(Long callTotal) {
        this.callTotal = callTotal;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public Long getInvalidAudioDuration() {
        return invalidAudioDuration;
    }

    public void setInvalidAudioDuration(Long invalidAudioDuration) {
        this.invalidAudioDuration = invalidAudioDuration;
    }

    public Long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    public Long getMinTime() {
        return minTime;
    }

    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }
}


