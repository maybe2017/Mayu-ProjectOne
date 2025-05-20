package com.mayu.common.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: 马瑜
 * @Date: 2023/9/4 21:06
 * @Description: redission相关配置
 **/
@Data
@Component
public class RedisProperties {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.timeout:2000}")
    private int timeout;
}
