package com.gosenor.component.impl;

import com.gosenor.component.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-22 14:54
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    /**
     * 配置⽂件中的发送邮箱
     */
    @Value("${spring.mail.from}")
    private String from;
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
//创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送⼈
        message.setFrom(from);
        //邮件接收⼈
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        mailSender.send(message);
        log.info("邮件发成功:{}",message.toString());
    }
}
