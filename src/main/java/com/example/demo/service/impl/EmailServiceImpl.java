package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Arrays;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
//    @KafkaListener(topics = "confirm-account-topic", groupId = "confirm-account-group")
    public void sendWelcomeEmail(String email) throws MessagingException {

        log.info("Sending mail ....");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        // mess = "email:minhtan0949@gmail.com,id:70,code:code@123";
//        String[] cat = messages.split(",");
//        String email = cat[0].substring(cat[0].indexOf(':') + 1);
//        String id = cat[1].substring(cat[1].indexOf(':') + 1);
//        String code = cat[2].substring(cat[2].indexOf(':') + 1);

        helper.setTo(email);
        helper.setSubject("Chào mừng bạn đến với hệ thống!");

        // Chuẩn bị context cho Thymeleaf
        Context context = new Context();
        context.setVariable("email", email); // gửi qua cho thằng template hứng

        String htmlContent = templateEngine.process("email/welcome-email", context);
        helper.setText(htmlContent, true); // true = is HTML

         mailSender.send(mimeMessage);
    }
}