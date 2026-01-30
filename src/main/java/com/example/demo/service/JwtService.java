package com.example.demo.service;

import com.example.demo.util.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface JwtService {
    // Sử dụng đúng cái UserDetail của hệ thống
    // đã được impl
    String generateAccessToken(Long userId, String username, Set<String> authorities);

    String generateRefreshToken(Long userId, String username, Set<String> authorities);

    String extractUsername(String token, TokenType type);

    boolean isValid(String token, TokenType tokenType, UserDetails userDetails);

    String generateResetToken(Long userId, String username, Set<String> authorities);
}
