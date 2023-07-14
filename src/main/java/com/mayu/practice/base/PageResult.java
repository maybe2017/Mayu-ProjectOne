package com.mayu.practice.base;

import lombok.Data;

import java.util.List;

/**
 * 分页结果对象
 */

@Data
public class PageResult<T> {


    private long total;

    private List<T> data;

    public PageResult() {
    }

    public PageResult(long total, List<T> data) {
        this.total = total;
        this.data = data;
    }
}