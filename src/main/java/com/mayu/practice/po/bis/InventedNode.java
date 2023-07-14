package com.mayu.practice.po.bis;

/**
 * 虚拟首节点
 */
public class InventedNode {
    private int partId = 0;             // 自定义id，从0开始，0表示虚拟首节点
    private String type = "0";          // 节点片段类型，0为虚拟首节点，1为通话节点
    private long audioDuration;         // 音频总时长，单位毫秒
    private long callDuration;          // 通话总时长，单位毫秒
    private long muteDuration;          // 静音总时长，单位毫秒
    private long csDuration;            // 客服总时长，单位毫秒
    private long userDuration;          // 用户总时长，单位毫秒
    private double csAvgSpeed;          // 客服平均语速，单位 字/秒
    private double userAvgSpeed;        // 用户平均语速，单位 字/秒
    private int nextPartId = 1;         // 下个片段id

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(long audioDuration) {
        this.audioDuration = audioDuration;
    }

    public long getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(long callDuration) {
        this.callDuration = callDuration;
    }

    public long getMuteDuration() {
        return muteDuration;
    }

    public void setMuteDuration(long muteDuration) {
        this.muteDuration = muteDuration;
    }

    public long getCsDuration() {
        return csDuration;
    }

    public void setCsDuration(long csDuration) {
        this.csDuration = csDuration;
    }

    public long getUserDuration() {
        return userDuration;
    }

    public void setUserDuration(long userDuration) {
        this.userDuration = userDuration;
    }

    public double getCsAvgSpeed() {
        return csAvgSpeed;
    }

    public void setCsAvgSpeed(double csAvgSpeed) {
        this.csAvgSpeed = csAvgSpeed;
    }

    public double getUserAvgSpeed() {
        return userAvgSpeed;
    }

    public void setUserAvgSpeed(double userAvgSpeed) {
        this.userAvgSpeed = userAvgSpeed;
    }

    public int getNextPartId() {
        return nextPartId;
    }

    public void setNextPartId(int nextPartId) {
        this.nextPartId = nextPartId;
    }

    public InventedNode() {
    }

    public InventedNode(int partId, String type, long audioDuration, long callDuration, long muteDuration, long csDuration, long userDuration, double csAvgSpeed, double userAvgSpeed, int nextPartId) {
        this.partId = partId;
        this.type = type;
        this.audioDuration = audioDuration;
        this.callDuration = callDuration;
        this.muteDuration = muteDuration;
        this.csDuration = csDuration;
        this.userDuration = userDuration;
        this.csAvgSpeed = csAvgSpeed;
        this.userAvgSpeed = userAvgSpeed;
        this.nextPartId = nextPartId;
    }
}
