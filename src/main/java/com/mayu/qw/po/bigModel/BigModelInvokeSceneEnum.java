package com.mayu.qw.po.bigModel;

import lombok.Getter;

/**
 * @Author: 马瑜
 * @Date: 2024/9/4 16:01
 * @Description: 大模型调用场景
 */
@Getter
public enum BigModelInvokeSceneEnum {

    /** 管家侧 100 - 1xx **/
    // 会话分类标记流程
    C_MIRCO_YX_TASK_RESULT(100, "管家-营销类型-任务结果检测"),
    C_MIRCO_FW_TASK_RESULT(101, "管家-服务类型-任务结果检测"),
    C_MIRCO_FL_TASK_RESULT(102, "管家-福利类型-任务结果检测"),

    C_MIRCO_SOLUTION_RESULT(110, "管家-解答情况检测"),

    // 营销任务话术检测
    C_MIRCO_MARKETING_RHETORIC_RESULT(120, "管家-营销任务话术检测"),

    // 管家对 提醒跟踪单 的跟进状态
    C_MIRCO_TRACE_YW_FOLLOW_STATUS(130, "管家-提醒跟踪单-业务-跟进状态"),
    C_MIRCO_TRACE_QX_FOLLOW_STATUS(131, "管家-提醒跟踪单-情绪-跟进状态"),

    /** 客户侧 200 - 2xx **/
    // 会话分类标记流程
    C_USER_TYPE(200, "客户-业务大类检测"),
    C_USER_SUBTYPE(201, "客户-业务子类检测"),


    C_USER_CONSULT_TYPE(220, "客户-咨询分类检测"),
    C_USER_BL_CONSULT_RESULT(221, "客户-办理咨询-咨询结果检测"),
    C_USER_SY_CONSULT_RESULT(222, "客户-使用咨询-咨询结果检测"),
    C_USER_WT_CONSULT_RESULT(223, "客户-问题咨询-咨询结果检测"),
    C_USER_TD_CONSULT_RESULT(224, "客户-退订咨询-咨询结果检测"),

    C_USER_SATISFACTION_FOR_MIRCO_SOLUTION(230, "客户-对管家解答情况-满意度检测");

    private final Integer code;
    private final String desc;

    BigModelInvokeSceneEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BigModelInvokeSceneEnum getInvokeSceneByDesc(String desc) {
        for (BigModelInvokeSceneEnum type : BigModelInvokeSceneEnum.values()) {
            if (type.getDesc().equals(desc)) {
                return type;
            }
        }
        throw new RuntimeException(String.format("BigModelInvokeSceneEnum枚举转换异常! desc:%s", desc));
    }

    public static BigModelInvokeSceneEnum getSceneByDescLike(String descKey) {
        for (BigModelInvokeSceneEnum type : BigModelInvokeSceneEnum.values()) {
            if (type.getDesc().contains(descKey)) {
                return type;
            }
        }
        throw new RuntimeException(String.format("BigModelInvokeSceneEnum枚举转换异常! descKey:%s", descKey));
    }

    public static BigModelInvokeSceneEnum getInvokeSceneByCode(Integer code) {
        for (BigModelInvokeSceneEnum type : BigModelInvokeSceneEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new RuntimeException(String.format("BigModelInvokeSceneEnum枚举转换异常! code:%s", code));
    }
}
