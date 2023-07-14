package com.mayu.practice.po.bis;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wttang
 * @Date: 2021/6/10
 * @Description:
 **/
@Data
public class ThirdParam implements Serializable {
    /**
     * 主叫号码
     */
    private String call;

    /**
     * 被叫号码
     */
    private String called;


    /**
     * callId
     */
    private String callId;


    /**
     * 通话开始时间
     */
    private String voiceTime;

    // 泡销数据新增
    /**
     * 泡销任务编号
     */
    private String pxTaskId;

    /**
     * 泡销用户号码
     */
    private String pxUserTel;

    /**
     * 泡销时间
     */
    private String pxVoiceTime;

    /**
     * 泡销时长（单位：秒）
     */
    private String pxDurationTime;

    /**
     * 泡销状态
     */
    private String pxStatus;
    /**
     * 泡销状态名称
     */
    private String pxStatusName;

    /**
     * 泡销类型
     */
    private String pxType;
    /**
     * 泡销类型名称
     */
    private String pxTypeName;

    /**
     * 创建人工号(泡销人员工号)
     */
    private String pxServiceId;

    /**
     * 录音流水号
     */
    private String voiceSeqNo;

    /**
     * 音频文件路径
     */
    private String voicePath;

    /**
     * 地市信息
     */
    private String city;

    /**
     * 区县信息
     */
    private String county;

    /**
     * 网格信息
     */
    private String grid;

    /**
     * 渠道
     */
    private String address;

    /**
     * 地市编号
     */
    private String cityId;

    /**
     * 区县编号
     */
    private String countyId;

    /**
     * 网格编号
     */
    private String gridId;

    /**
     * 渠道编号
     */
    private String addressId;

    /**
     * 录音类型名称
     */
    private String voiceTypeName;
    /**
     * 录音类型
     */
    private String voiceType;
    // 7、地市
    private String cityName;
    /**
     * 8、渠道编码
     */
    private String channelCode;
    /**
     * 9、BOSS工号
     */
    private String bossServiceId;
    /**
     * 10、工号姓名
     */
    private String serviceName;
    /**
     * 11、业务流水
     */
    private String bussinessId;
    /**
     * 12、业务类型
     */
    private String bussinessType;
    /**
     * 13、客户号码
     */
    private String customerTel;
}
