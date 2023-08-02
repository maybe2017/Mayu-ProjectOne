package com.mayu.practice.test;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

/**
 * @Author: 马瑜
 * @Date: 2023/6/27 17:04
 * @Description:
 */
@Slf4j
public class Test01 {
    public static void main(String[] args) {
//        testEor();
        handleStr();
    }

    // 异或的测试
    private static void testEor() {
        int i = 999;
        int j = 888;
        System.out.println(i ^ i ^ j);
        System.out.println(0 ^ i ^ j ^ i ^ j ^ i);
    }

    // 加密配置文件中的字符串
    private static String handleStr() {
        double d = Double.NaN;
        return "";
    }
}
