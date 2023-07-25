package com.mayu.practice.controller;

import com.alibaba.fastjson.JSON;
import com.mayu.practice.base.ResponseMap;
import com.mayu.practice.po.req.DaZhongConfigEncryptReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 马瑜
 * @Date: 2023/7/25 16:28
 * @Description: 一些工作接口
 */
@Slf4j
@RestController
@RequestMapping("/dazhong")
public class SelfController {
    @PostMapping(value = "/config/encrypt")
    public ResponseMap configEncrypt(@RequestBody DaZhongConfigEncryptReq req) {
        Map<String, String> resMap = new HashMap<>();
        log.info("接收到加密请求! {}", JSON.toJSONString(req));

        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("encryptPassword_iflytek0725");
        standardPBEStringEncryptor.setConfig(config);

        if (CollectionUtils.isNotEmpty(req.getOriginStrList())) {
            for (String originStr : req.getOriginStrList()) {
                if (StringUtils.isNotBlank(originStr)) {
                    String encrypted = standardPBEStringEncryptor.encrypt(originStr);
                    resMap.put(originStr, encrypted);
                    log.info("原始字符串: {}, 加密后字符串: {}", originStr, encrypted);
                }
            }
        }
        return ResponseMap.success(resMap);
    }
}
