package com.mayu.practice.po;

import lombok.Data;

/**
 * @Author: 马瑜
 * @Date: 2023/3/3 10:42
 * @Description: TODO
 */
@Data
public class DuplicateKeyDto {
    private String _id;
    private String taskId;
    private Integer count;

}
