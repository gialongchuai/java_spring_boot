package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendWelcomeEmail(User user) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(user.getEmail());
        helper.setSubject("Chào mừng bạn đến với hệ thống!");

        // Chuẩn bị context cho Thymeleaf
        Context context = new Context();
        context.setVariable("user", user);

        String htmlContent = templateEngine.process("email/welcome-email", context);
        helper.setText(htmlContent, true); // true = is HTML

        mailSender.send(mimeMessage);
    }
}