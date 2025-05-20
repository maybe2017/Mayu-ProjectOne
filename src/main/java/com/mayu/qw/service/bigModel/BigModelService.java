package com.mayu.qw.service.bigModel;

import com.mayu.qw.po.bigModel.BigModelInvokeSceneEnum;
import com.mayu.qw.po.bigModel.InvokeBMCustomParams;

/**
 * @Author: 马瑜
 * @Date: 2024/9/4 16:13
 * @Description: 大模型调用
 */
public interface BigModelService {

    /**
     * 调用大模型检测
     * @param sceneEnum 检测场景
     * @param conversationId 会话id
     * @param prompt 提示词
     * @return 大模型返回原始结果
     */
    String detectInvoke(BigModelInvokeSceneEnum sceneEnum, String conversationId, String prompt);

    /**
     * 调用大模型检测
     * @param sceneEnum 检测场景
     * @param conversationId 会话id
     * @param prompt 提示词
     * @param customParams 调用大模型的一些自定义参数
     * @return 大模型返回原始结果
     */
    String detectInvoke(BigModelInvokeSceneEnum sceneEnum, String conversationId, String prompt, InvokeBMCustomParams customParams);

}
