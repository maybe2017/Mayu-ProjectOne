package com.mayu.qw.service.bigModel;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mayu.practice.mapper.BigModelInvokeConfigMapper;
import com.mayu.practice.utils.HttpUtils;
import com.mayu.qw.config.bigModel.BigModelConfig;
import com.mayu.qw.po.bigModel.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: 马瑜
 * @Date: 2024/9/4 15:47
 * @Description: 大模型调用
 *  TODO 1. 多个大模型key的配置，根据场景区分 ?
 *  TODO 2. QPS调用频率的控制？(每秒调用2次，若调用限流失败，则重新调用1次(失败重试)，若调用仍然失败，则结果置空即可)
 */
@Service
@Slf4j
public class BigModelServiceImpl implements BigModelService {

    @Resource
    private BigModelConfig bigModelConfig;
    @Resource
    private BigModelInvokeConfigMapper bigModelInvokeConfigMapper;

    /**
     * 映射关系: 场景code -> 大模型版本
     **/
    private static Map<Integer, BigModelInvokeConfig> sceneCode2InvokeConfigMap;

    @Override
    public String detectInvoke(BigModelInvokeSceneEnum sceneEnum, String conversationId, String prompt) {
        return detectInvoke(sceneEnum, conversationId, prompt, null);
    }

    @Override
    public String detectInvoke(BigModelInvokeSceneEnum sceneEnum, String conversationId, String prompt, InvokeBMCustomParams customParams) {
        if (StringUtils.isBlank(prompt)) {
            return null;
        }
        log.info("【大模型-{}[{}]】prompt：{}", sceneEnum.getDesc(), conversationId, prompt);

        long start = System.currentTimeMillis();
        String res = doInvoke(conversationId, prompt, customParams, getBigModelInvokeConfig(sceneEnum.getCode()));
        long end6 = System.currentTimeMillis();
        log.info("6-[{}]-[{}]，调用大模型耗时：{}ms", conversationId, sceneEnum.getDesc(), end6 - start);

        return res;
    }

    /**
     * 调用大模型HTTP接口
     *
     * @param conversationId 会话id
     * @param prompt         提示词
     * @param customParams   调用大模型API时 一些自定义参数
     * @param invokeConfig   大模型配置(eg: 大模型版本max、lite等)；如果为空，则用配置文件中的默认值
     * @return 大模型返回字符串
     */
    private String doInvoke(String conversationId, String prompt, InvokeBMCustomParams customParams, BigModelInvokeConfig invokeConfig) {
        InvokeBigModelHttpReq req = buildInvokeReqWithCustomParam(customParams);
        // 大模型版本
        req.setModel(calcInvokeVersion(invokeConfig));
        req.setMessages(buildReqBody(prompt));
        Map<String, String> headerMap = buildAuthorizationHeader(invokeConfig);
        log.info("[{}]-[{}]大模型调用开始! [{}] - [{}] - [{}]", conversationId, req.getModel(), invokeConfig.getBusinessSceneDesc(), JSON.toJSONString(headerMap), JSON.toJSONString(req));
        HttpUtils.Response<Map> response = HttpUtils.doPost(bigModelConfig.getHttpUrl(), headerMap, null, JSON.toJSONString(req), Map.class);
        if (response.getCode() == 200) {
            Map data = response.getData();
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(response.getData()));
            Integer returnCode = (Integer) data.get("code");
            if (returnCode == 0) {
                log.info("大模型[{}]调用成功[{}]! 响应: {}", req.getModel(), conversationId, JSON.toJSONString(data));
                Object choices = data.get("choices");
                if (null != choices) {
                    JSONArray jsonArray = jsonObject.getJSONArray("choices");
                    JSONObject item = jsonArray.getJSONObject(0);
                    JSONObject messageObj = item.getJSONObject("message");
                    String content = messageObj.getString("content");
                    return content.replaceAll("\n", "");
                }
            } else {
                log.error("大模型调用异常[{}]! 响应: {}", conversationId, JSON.toJSONString(response));
            }
        }
        return null;
    }

    private InvokeBigModelHttpReq buildInvokeReqWithCustomParam(InvokeBMCustomParams customParams) {
        InvokeBigModelHttpReq req = new InvokeBigModelHttpReq();
        if (null != customParams) {
            Float temperature = customParams.getTemperature();
            Integer max_tokens = customParams.getMax_tokens();
            Integer top_k = customParams.getTop_k();
            if (null != temperature) {
                req.setTemperature(temperature);
            }
            if (null != max_tokens) {
                req.setMax_tokens(max_tokens);
            }
            if (null != top_k) {
                req.setTop_k(top_k);
            }
        }
        return req;
    }

    private List<InvokeBigModelMsgItem> buildReqBody(String onceConversationPrompt) {
        List<InvokeBigModelMsgItem> reqBody = new ArrayList<>();
        InvokeBigModelMsgItem item = new InvokeBigModelMsgItem();
        item.setRole("user");
        item.setContent(onceConversationPrompt);
        reqBody.add(item);
        return reqBody;
    }

    // 保证重跑会话分类流程使用max版本
    private String calcInvokeVersion(BigModelInvokeConfig invokeConfig) {
        boolean retry = false;
        String bigModelVersion = bigModelConfig.getBigModeVersion();
        String bigModelVersion2 = bigModelConfig.getBigModeVersion2();
        String firstResVersion = retry ? (StringUtils.isNotBlank(bigModelVersion2) ? bigModelVersion2 : bigModelVersion) : bigModelVersion;
        // 优先考虑重跑标记
        if (retry) {
            return firstResVersion;
        }

        // 正常流程下，以数据库配置为优先
        if (null != invokeConfig && StringUtils.isNotBlank(invokeConfig.getInvokeVersion())) {
            return invokeConfig.getInvokeVersion();
        }

        return firstResVersion;
    }

    // 保证重跑会话分类流程使用max版本
    private Map<String, String> buildAuthorizationHeader(BigModelInvokeConfig invokeConfig) {
        boolean retry = false;
        String apiKey = retry ? (StringUtils.isNotBlank(bigModelConfig.getApiKey2()) ? bigModelConfig.getApiKey2() : bigModelConfig.getApiKey()) :
                bigModelConfig.getApiKey();
        String apiSecret = retry ? (StringUtils.isNotBlank(bigModelConfig.getApiSecret2()) ? bigModelConfig.getApiSecret2() : bigModelConfig.getApiSecret()) :
                bigModelConfig.getApiSecret();
        // 优先重跑
        if (retry) {
            return doBuildAuthorizationHeader(apiKey, apiSecret);
        }

        // 正常流程下，以数据库配置为优先
        if (null != invokeConfig && StringUtils.isNotBlank(invokeConfig.getInvokeVersion())) {
            String invokeVersion = invokeConfig.getInvokeVersion();
            if (Arrays.asList("generalv3.5", "GENERALV3.5", "max", "MAX").contains(invokeVersion)) {
                return doBuildAuthorizationHeader(bigModelConfig.getApiKey2(), bigModelConfig.getApiSecret2());
            } else if (Arrays.asList("lite", "LITE").contains(invokeVersion)) {
                return doBuildAuthorizationHeader(bigModelConfig.getApiKey(), bigModelConfig.getApiSecret());
            }
        }

        return doBuildAuthorizationHeader(apiKey, apiSecret);
    }

    private Map<String, String> doBuildAuthorizationHeader(String apiKey, String apiSecret) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json; charset=UTF-8");
        header.put("Authorization", String.format("Bearer %s:%s", apiKey, apiSecret));
        return header;
    }


    private BigModelInvokeConfig getBigModelInvokeConfig(Integer sceneCode) {
        try {
            if (MapUtil.isEmpty(sceneCode2InvokeConfigMap)) {
                List<BigModelInvokeConfig> bigModelInvokeConfigList = bigModelInvokeConfigMapper.selectList(new QueryWrapper<>());
                sceneCode2InvokeConfigMap = bigModelInvokeConfigList.stream().collect(Collectors.toMap(BigModelInvokeConfig::getBusinessSceneCode, Function.identity()));
            }
            log.info("*****初始化大模型调用配置成功: {}条 ****", sceneCode2InvokeConfigMap.size());
        } catch (Exception e) {
            log.error("*****初始化大模型调用配置失败: {}****", e.getMessage(), e);
        }
        if (MapUtil.isNotEmpty(sceneCode2InvokeConfigMap)) {
            return sceneCode2InvokeConfigMap.get(sceneCode);
        }
        return null;
    }
}
