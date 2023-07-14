package com.mayu.practice.po.bis;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ResourceAttr {
    /**
     * 流水号
     */
    private String seqNo;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 业务线名称
     */
    private String bizLink;
    /**
     * 客服编号
     */
    private String serviceId;
    /**
     * 呼入（呼出）号码
     */
    private String tel;
    /**
     * 城市
     */
    private String city;
    /**
     * 省
     */
    private String province;
    /**
     * 1、呼入  2、呼出
     */
    private Integer callType;
    /**
     * 语音资源定位符
     */
    private String voiceUrl;
    /**
     * 语音发生时间
     */
    private LocalDateTime voiceTime;
    /**
     * 存储转码为pcm的音频路径
     */
    private String pcmUrl;
    /**
     * 存储转码为mp3的音频路径
     */
    private String mp3Url;
    /**
     * 存储原始音频的fdfs的音频路径
     */
    private String original;
    /**
     * 音频格式
     */
    private String format;
    /**
     * 需要指定参数的格式 接口或者ftp传入
     */
    private String formatKey;
    /**
     * 音频原始采样率,8=8000Hz, 16=160000Hz
     */
    private Integer rate;
    /**
     * 分析结果返回格式
     */
    private String rst;
    /**
     * 待转写音频声道,mono=单声道， stereo=双声道
     */
    private String sc;
    /**
     * 平台透传字段，结果通知接口返回，用于业务方自身业务
     */
    private String ext;
    /**
     * 8k转写引擎返回的任务id
     */
    private String taskId;
    /**
     * 8k引擎需要上传pcm到指定ftp
     */
    private String pcmFtpUrl;
    /**
     * 分析结果回调地址
     */
    private String resultUrl;
    /**
     * 音频大小
     */
    private long audioSize;
    /**
     * 音频时长
     */
    private int duration;
    /**
     * 音量数组，数组 下标+1 对应音频第几秒的音量平均值
     * 例如 数组下标为2 表示第3秒的音量平均值
     * 音量保留两位小数，格式化后保存为String类型
     */
    private String[] volumes;
}
