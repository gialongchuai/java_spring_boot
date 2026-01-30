package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.JwtService;
import com.example.demo.util.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryHour}")
    private String expiryHour;

    @Value("${jwt.expiryDay}")
    private String expiryDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Value("${jwt.resetKey}")
    private String resetKey;

    @Override
    public String generateAccessToken(Long userId, String username, Set<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);
        return generateAccessToken(claims, username);
    }

    @Override
    public String generateRefreshToken(Long userId, String username, Set<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);
        return generateRefreshToken(claims, username);
    }

    @Override
    public String generateResetToken(Long userId, String username, Set<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);
        return generateResetToken(claims, username);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaims(token, type, Claims::getSubject);
    }

    // Valid cho token trong preFilter trước khi vào các request
    @Override
    public boolean isValid(String token, TokenType tokenType, UserDetails userDetails) {
        // Lấy username trong token thông qua subject của token
        String username = extractUsername(token, tokenType);

        // xem coi cái username có giống với useDetails không
        // và xem có còn hạn hay không ... và sẽ làm bc này sau
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType); // username và chưa hết hạn
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaims(token, type, Claims::getExpiration);
    }

    private String generateAccessToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims) // những thông tin payload không muốn public ra ngoài, chỉ hiện dưới dạng mã hóa như email, phone,
                .setSubject(username) // để ko trùng lặp
                .setIssuedAt(new Date(System.currentTimeMillis())) // ngày tạo ra token này
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * Long.parseLong(expiryHour)))) // giờ hệ thống + (Giờ hết hạn ex: expiryHour: 1 thì 1hour hết hạn token
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * Long.parseLong(expiryDay))))
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateResetToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60)))
                .signWith(getKey(TokenType.RESET_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        byte[] keyBytes;
        switch (type) {
            case ACCESS_TOKEN -> keyBytes = Decoders.BASE64.decode(accessKey);
            case REFRESH_TOKEN -> keyBytes = Decoders.BASE64.decode(refreshKey);
            case RESET_TOKEN -> keyBytes = Decoders.BASE64.decode(resetKey);
            default -> throw new ResourceNotFoundException("Token type is invalid!");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaims(String token, TokenType type, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, type);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType type) {
        try {
            return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException | SignatureException ex) {
            throw new ResourceNotFoundException("Access Denied with error messages: " + ex.getMessage());
        }
    }
}
