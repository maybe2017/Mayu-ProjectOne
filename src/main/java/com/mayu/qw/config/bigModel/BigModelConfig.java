package com.mayu.qw.config.bigModel;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 马瑜
 * @Date: 2024/09/02 16:17
 * @Description: 大模型接入
 */
@Slf4j
@Configuration
@Data
public class BigModelConfig {

    @Value("${bigModel.httpUrl:http://192.168.30.2:10073/v1/chat/completions}")
    private String httpUrl;
    @Value("${bigModel.printPromptLog:false}")
    private boolean printPromptLog;
    @Value("${bigModel.enableRateLimit:true}")
    private boolean enableRateLimit;
    @Value("${bigModel.permitsPerSecond:2}")
    private Integer permitsPerSecond;

    @Value("${bigModel.configOne.apiKey}")
    private String apiKey;
    @Value("${bigModel.configOne.apiSecret}")
    private String apiSecret;
    @Value("${bigModel.configOne.bigModeVersion}")
    private String bigModeVersion;

    @Value("${bigModel.configTwo.apiKey:}")
    private String apiKey2;
    @Value("${bigModel.configTwo.apiSecret:}")
    private String apiSecret2;
    @Value("${bigModel.configTwo.bigModeVersion:}")
    private String bigModeVersion2;
}
