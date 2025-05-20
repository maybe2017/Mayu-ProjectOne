package com.mayu.qw.controller;

import com.mayu.qw.service.CheckMircoTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: 马瑜
 * @Date: 2024/10/11 16:16
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/generalMarketingTask")
public class TestGeneralMarketingTaskController {

    @Resource
    private CheckMircoTaskService checkMircoTaskService;

    @GetMapping(value = "/tryMatch")
    public String testGeneralMarketingReplyInfo(String taskId) {
        return checkMircoTaskService.testGeneralMarketingReplyInfo(taskId);
    }
}
