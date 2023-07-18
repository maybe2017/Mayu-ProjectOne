package com.mayu.practice.test;


import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 马瑜
 * @Date: 2023/6/27 17:04
 * @Description:
 */
@Slf4j
public class Test01 {
    public static void main(String[] args) {
        testEor();
    }

    // 异或的测试
    private static void testEor() {
        int i = 999;
        int j = 888;
        System.out.println(i ^ i ^ j);
        System.out.println(0 ^ i ^ j ^ i ^ j ^ i);
    }
}
