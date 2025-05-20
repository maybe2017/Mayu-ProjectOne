package com.mayu.practice.test.dto;

import lombok.Data;

/**
 * @Author: 马瑜
 * @Date: 2024/2/22 16:27
 * @Description: 聚合结果
 */
@Data
public class DupGroupCount {
    private String _id;
    private String micName;
    private String cityName;
    private int dupCount;
}
