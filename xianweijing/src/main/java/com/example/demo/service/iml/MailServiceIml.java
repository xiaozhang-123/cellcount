package com.example.demo.service.iml;

import com.example.demo.service.MailService;
import com.example.demo.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author zhangduhuang
 * @date 2021/7/18 0:11
 */
@Slf4j
@Service
public class MailServiceIml implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RedisService redisService;
    @Value("${spring.mail.username}")
    private String from;
    /**
     *
     *
      * @Param: email
     * @Param: username
     * @return boolean
    */
    @Override
    public boolean sendMail(String email, String username) {
        Random random = new Random(System.currentTimeMillis());
        int code=random.nextInt(10000);
        redisService.saveByTime(username,code+"",60);
        log.info("验证码追踪:"+username+" "+code);
        sendSimpleMail(email,"账号激活验证邮件","账号激活激活码为："+code+" 请于60s内完成");
        return true;
    }

    public void sendSimpleMail(String to,String subject,String content){
        SimpleMailMessage message=new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(from);
        //对象给发送器
        log.info("发送邮件: from "+from+" to "+to);
        mailSender.send(message);
    }
}
