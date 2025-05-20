package com.mayu.practice;

import com.alibaba.fastjson.JSON;
import com.mayu.NeptuneApplication;
import com.mayu.practice.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @Author: 马瑜
 * @Date: 2023/6/20 14:09
 * @Description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeptuneApplication.class)
public class EmailTest {

    @Resource
    private EmailService emailService;

    @Test
    public void testSend() {
        emailService.sendEmail(JSON.toJSONString(Arrays.asList("msgId_AAAAAA", "msgId_BBBB")));
    }
}
