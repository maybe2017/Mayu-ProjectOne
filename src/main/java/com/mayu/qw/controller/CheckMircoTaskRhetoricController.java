package com.mayu.qw.controller;

import com.mayu.qw.po.req.CheckMarketingRhetoricReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 马瑜
 * @Date: 2024/10/15 16:38
 * @Description: 对管家营销话术进行校验
 */
@Slf4j
@RestController
@RequestMapping("/mirco")
public class CheckMircoTaskRhetoricController {

    @PostMapping(value = "/marketing/rhetoric/check")
    public String checkMarketingRhetoric(@RequestBody CheckMarketingRhetoricReq rhetoricReq) {

        return "";
    }
}
