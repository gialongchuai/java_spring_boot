package com.example.demo.service;

import com.example.demo.model.User;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendWelcomeEmail(String email) throws MessagingException;
}
