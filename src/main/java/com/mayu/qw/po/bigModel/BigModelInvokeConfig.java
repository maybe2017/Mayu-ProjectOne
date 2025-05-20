package com.mayu.qw.po.bigModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Author: 马瑜
 * @Date: 2024/10/22 16:52
 * @Description: 以大模型判定某业务的配置
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class BigModelInvokeConfig {

    // 主键
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 业务编码 BigModelInvokeSceneEnum
    private Integer businessSceneCode;

    // 业务描述
    private String businessSceneDesc;

    // 提示词
    private String prompt;

    // 预定结果项；多个框定项质检用中文；分割
    private String preValidValues;

    /**
     * 大模型回复长度限制
     * 默认2048
     */
    private Integer limitLength;

    /**
     * 大模型灵活度
     * 默认4
     */
    @TableField(value = "topK")
    private String topK;

    /**
     * 大模型随机性
     * 默认0.5
     */
    private String temperature;

    /**
     * 使用大模型的版本
     * max: generalv3.5  => 对应配置文件中配置1
     * lite: lite => 对应配置文件中配置2
     */
    private String invokeVersion;

    /**
     * 是否删除(0正常、-1 已经删除)
     */
    private Integer deleteFlag;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人姓名[快照字段]
     */
    private String updateUserName;

}
