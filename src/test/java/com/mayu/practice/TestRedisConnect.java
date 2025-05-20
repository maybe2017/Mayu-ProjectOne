package com.mayu.practice;

import com.mayu.NeptuneApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: 马瑜
 * @Date: 2023/8/14 18:26
 * @Description: TODO
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeptuneApplication.class)
public class TestRedisConnect {

    private static final String TASK_KEY = "mayu";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testSend() {
        redisTemplate.opsForList().leftPush(TASK_KEY, 1111111);
    }
}
