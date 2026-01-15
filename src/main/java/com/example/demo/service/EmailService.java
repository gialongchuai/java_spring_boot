package com.example.demo.service;

import com.example.demo.model.User;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendWelcomeEmail(User user) throws MessagingException;
}
