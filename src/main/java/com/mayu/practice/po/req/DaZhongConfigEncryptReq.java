package com.mayu.practice.po.req;

import lombok.Data;

import java.util.List;

/**
 * @Author: 马瑜
 * @Date: 2023/7/25 16:30
 * @Description: 大众对线上配置的加密
 */
@Data
public class DaZhongConfigEncryptReq {

    private List<String> originStrList;
}
