package com.mayu.practice.po.csv;

import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/**
 * @Author: 马瑜
 * @Date: 2025/3/14 17:11
 * @Description: 读取csv
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CsvColumn {
    /**
     * 第几列
     */
    @NotNull
    int columnIndex();
}
