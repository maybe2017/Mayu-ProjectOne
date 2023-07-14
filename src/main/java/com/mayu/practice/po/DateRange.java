package com.mayu.practice.po;

import lombok.Data;

/**
 * @Author: 马瑜
 * @Date: 2023/6/25 11:17
 * @Description: 时间区间
 */
@Data
public class DateRange {
    public DateRange(String leftDate, String rightDate) {
        this.leftDate = leftDate;
        this.rightDate = rightDate;
    }
    private String leftDate;
    private String rightDate;
}
