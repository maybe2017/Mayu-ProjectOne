package com.mayu.qw.po.bigModel;

import lombok.Data;

/**
 * @Author: 马瑜
 * @Date: 2024/9/5 10:47
 * @Description: 大模型调用 自定义参数
 */
@Data
public class InvokeBMCustomParams {

    private Float temperature;
    private Integer max_tokens;
    private Integer top_k;
}

