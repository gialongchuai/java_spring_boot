package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    // Sử dụng đúng cái UserDetail của hệ thống
    // đã được impl
    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUsername(String token);

    boolean isValid(String token, UserDetails userDetails);
}
