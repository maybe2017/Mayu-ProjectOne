package com.mayu.practice.po.csv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 马瑜
 * @Date: 2025/3/14 17:52
 * @Description: 入户
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RhOrderInfo {

    // 上游数据类型[1:prebook预约; 2:installation入户]
    private Integer bizType;

    // 录音编号 如果上游传为空 则自动生成？or之后取话单编号？
    private String seqNo;

    // 录音文件名称
    private String audioFileName;

    // 录音时间 yyyy/MM/dd HH:mm 格式
    private String voiceTime;

    // 地市名称
    @CsvColumn(columnIndex = 2)
    private String cityName;

    // 装维工号
    @CsvColumn(columnIndex = 3)
    private String zwNo;

    // 装维手机号
    @CsvColumn(columnIndex = 4)
    private String zwPhone;

    // 用户手机号
    @CsvColumn(columnIndex = 5)
    private String userPhone;

    // 工单编号
    @CsvColumn(columnIndex = 6)
    private String orderNo;

    // 工单类型
    @CsvColumn(columnIndex = 7)
    private String orderType;
    // 业务类型
    @CsvColumn(columnIndex = 8)
    private String orderBisType;
    // 业务类别
    @CsvColumn(columnIndex = 9)
    private String orderBisClass;
    // 代维单位(班组)
    @CsvColumn(columnIndex = 10)
    private String zwGroup;

    // <入户新增字段>

    // 宽带回单结果
    @CsvColumn(columnIndex = 11)
    private String bbReceiptRes;
    // 机顶盒回单结果
    @CsvColumn(columnIndex = 12)
    private String stbReceiptRes;
    // IPTV回单结果
    @CsvColumn(columnIndex = 13)
    private String iptvReceiptRes;
    // 智能组网回单结果
    @CsvColumn(columnIndex = 14)
    private String inReceiptRes;

    // <质检流程-业务字段>
    // 录音文件本地路径
    private String audioFileLocalPath;
    private String audioFileUrl;

    // 数据提交状态
    private String submitStatus;
    private String submitFailMsg;

    private String createTime;
    private String updateTime;
}
