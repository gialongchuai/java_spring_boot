package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.util.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    // Sử dụng đúng cái UserDetail của hệ thống
    // đã được impl
    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUsername(String token, TokenType type);

    boolean isValid(String token, TokenType tokenType, UserDetails userDetails);

    String generateResetToken(User user);
}
