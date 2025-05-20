package com.mayu.qw.po.bigModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mayu.practice.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DeepSeekV3Demo {
    public static void main(String[] args) {
        List<InvokeBigModelMsgItem> messages = new ArrayList<>();
        InvokeBigModelMsgItem msgItem = new InvokeBigModelMsgItem();
        msgItem.setRole("user");
        msgItem.setContent("你会做什么？");
        messages.add(msgItem);
        InvokeDeepSeekV3ModelHttpReq req = new InvokeDeepSeekV3ModelHttpReq();
        req.setMessages(messages);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = null;
        try {
//            HttpUtils.Response<Map> response = HttpUtils.doPost(bigModelConfig.getHttpUrl(), headerMap, null, JSON.toJSONString(req), Map.class);

            HttpHeaders headerMap = new HttpHeaders();
            headerMap.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headerMap.set("Authorization", String.format("Bearer %s", "sk-DsQNq0T9hkSph5FWF700717984D148BdAe4d3c8bC9D10258"));
            HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(req), headerMap);

            response = restTemplate.postForEntity("https://maas-api.cn-huabei-1.xf-yun.com/v1/chat/completions", entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map data = response.getBody();
                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(response.getBody()));
                log.info("大模型[{}]调用成功! 响应: {}", req.getModel(), JSON.toJSONString(data));
                Object choices = data.get("choices");
                if (null != choices) {
                    JSONArray jsonArray = jsonObject.getJSONArray("choices");
                    JSONObject item = jsonArray.getJSONObject(0);
                    JSONObject messageObj = item.getJSONObject("message");
                    String content = messageObj.getString("content");
                    log.info("大模型[{}]调用成功! 响应: {}", req.getModel(), content.replaceAll("\n", ""));
                }
            }
        } catch (Exception e) {
            log.error("大模型调用异常! 响应: {}", JSON.toJSONString(response));
        }

//        HttpUtils.Response<Map> response = HttpUtils.doPost("https://maas-api.cn-huabei-1.xf-yun.com/v1/chat/completions", buildAuthorizationHeader(),
//                null, JSON.toJSONString(req), Map.class);
//        if (response.getCode() == 200) {
//            Map data = response.getData();
//            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(response.getData()));
//            log.info("大模型[{}]调用成功! 响应: {}", req.getModel(), JSON.toJSONString(data));
//            Object choices = data.get("choices");
//            if (null != choices) {
//                JSONArray jsonArray = jsonObject.getJSONArray("choices");
//                JSONObject item = jsonArray.getJSONObject(0);
//                JSONObject messageObj = item.getJSONObject("message");
//                String content = messageObj.getString("content");
//                log.info("大模型[{}]调用成功! 响应: {}", req.getModel(), content.replaceAll("\n", ""));
//            }
//        }


//        HttpUtils.Response<Map> response = HttpUtils.doPost("https://maas-api.cn-huabei-1.xf-yun.com/v1/chat/completions",
//                buildAuthorizationHeader(), null,
//                JSON.toJSONString(req), Map.class);
//        if (response.getCode() == 200) {
//            JSONArray returnChoices = JSONObject.parseObject(JSON.toJSONString(response.getData())).getJSONArray("choices");
//            if (null != returnChoices && returnChoices.size() != 0){
//                JSONObject returnChoice = JSON.parseObject(String.valueOf(returnChoices.get(0)));
//                if (null != returnChoice){
//                    JSONObject returnMessage = returnChoice.getJSONObject("message");
//                    String content = returnMessage.getString("content");
//                    System.out.println(content);
//                }else {
//                    // TODO: 2025/4/24 打印日志
//                    System.out.println("大模型调用异常! 响应:" + JSON.toJSONString(response));
//                }
//            }else {
//                // TODO: 2025/4/24 打印日志
//                System.out.println("大模型调用异常! 响应:" + JSON.toJSONString(response));
//            }
//        }else {
//            // TODO: 2025/4/24 打印日志
//            System.out.println("大模型调用异常! 响应:" + JSON.toJSONString(response));
//        }
    }

    private static Map<String, String> buildAuthorizationHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json; charset=UTF-8");
        // sk-DsQNq0T9hkSph5FWF700717984D148BdAe4d3c8bC9D10258
        header.put("Authorization", String.format("Bearer %s", "sk-DsQNq0T9hkSph5FWF700717984D148BdAe4d3c8bC9D10258"));
        return header;
    }
}
