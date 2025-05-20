package com.mayu.practice;

import com.mayu.NeptuneApplication;
import com.mayu.practice.config.FtpConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: 马瑜
 * @Date: 2024/8/19 10:34
 * @Description: 测试
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeptuneApplication.class)
public class SemaphoreTest {

    @Resource
    private FtpConfig ftpConfig;

    @Test
    public void testAgg() {
        ftpConfig.doHandle();
        ftpConfig.doHandle();
        ftpConfig.doHandle();
        ftpConfig.doHandle();
    }
}
