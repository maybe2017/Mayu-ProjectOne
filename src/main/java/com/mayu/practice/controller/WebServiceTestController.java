package com.mayu.practice.controller;

import com.mayu.practice.service.impl.UserInfoApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: 马瑜
 * @Date: 2024/7/22 11:10
 * @Description: 模拟webservice请求
 */
@Slf4j
@RestController
@RequestMapping("/webservice")
public class WebServiceTestController {

    @Resource
    private UserInfoApiClient userInfoApiClient;

    @RequestMapping("/queryUsers")
    public Object queryUsers() throws Exception {
        return userInfoApiClient.queryUsersWithDynamic();
    }
}
