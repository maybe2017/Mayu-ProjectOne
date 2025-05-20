package com.mayu.practice;

import com.mayu.NeptuneApplication;
import com.mayu.qw.service.CheckMircoTaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: 马瑜
 * @Date: 2024/10/11 16:02
 * @Description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeptuneApplication.class)
public class CheckMircoTaskServiceTest {

    @Resource
    private CheckMircoTaskService checkMircoTaskService;

    @Test
    public void testGeneralMarketingReplyInfo() {
        String taskId = "100051164";
        String taskId2 = "100051165";
        checkMircoTaskService.testGeneralMarketingReplyInfo(taskId);
    }
}
