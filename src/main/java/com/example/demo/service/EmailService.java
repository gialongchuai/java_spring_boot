package com.example.demo.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendWelcomeEmail(String email) throws MessagingException;
}
