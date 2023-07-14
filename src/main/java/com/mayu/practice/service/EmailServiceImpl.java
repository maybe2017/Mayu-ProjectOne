package com.mayu.practice.service;

import com.mayu.practice.config.EmailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

/**
 * @Author: 马瑜
 * @Date: 2023/6/20 11:22
 * @Description:
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private EmailConfig emailConfig;

    @Override
    public boolean sendEmail(String emailText) {
        String sendToEmails = emailConfig.getSendTo();
        String[] sendToEmailArr = Arrays.stream(sendToEmails.split(",")).map(String::trim).toArray(String[]::new);

        log.info("发送消息推送告警邮件! 接收者邮件地址详细: {}", sendToEmails);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setSubject(emailConfig.getSubject());
        msg.setFrom(emailConfig.getMailUsername());
        msg.setTo(sendToEmailArr);
        msg.setSentDate(new Date());
        msg.setText(emailText);
        try {
            javaMailSender.send(msg);
            log.info("告警邮件发送成功!");
            return true;
        } catch (Exception e) {
            log.error("邮件发送异常! {}", e.getMessage(), e);
            return false;
        }
    }
}
