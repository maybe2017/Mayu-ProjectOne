package com.mayu.practice.po;

import lombok.Data;

/**
 * @Author: 马瑜
 * @Date: 2023/2/24 10:15
 * @Description:
 */
@Data
public class Params {

    private String name;
    private String phone;
    private Params testParam;
}
