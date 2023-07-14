package com.mayu.practice.service;

/**
 * @Author: 马瑜
 * @Date: 2023/6/20 11:21
 * @Description: 邮箱服务
 */
public interface EmailService {

    // 发送简单邮件
    boolean sendEmail(String emailText);
}
