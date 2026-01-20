package com.example.demo.service.impl;

import com.example.demo.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryHour}")
    private String expiryHour;

    @Value("${jwt.expiryDay}")
    private String expiryDay;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public String generateAccessToken(UserDetails userDetails) {
       return generateAccessToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // Valid cho token trong preFilter trước khi vào các request
    @Override
    public boolean isValid(String token, UserDetails userDetails) {
        // Lấy username trong token thông qua subject của token
        String username = extractUsername(token);

        // xem coi cái username có giống với useDetails không
        // và xem có còn hạn hay không ... và sẽ làm bc này sau
        return username.equals(userDetails.getUsername());
    }

    private String generateAccessToken(Map<String, Objects> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims) // những thông tin payload không muốn public ra ngoài, chỉ hiện dưới dạng mã hóa như email, phone,
                .setSubject(userDetails.getUsername()) // để ko trùng lặp
                .setIssuedAt(new Date(System.currentTimeMillis())) // ngày tạo ra token này
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * Long.parseLong(expiryHour)))) // giờ hệ thống + (Giờ hết hạn ex: expiryHour: 1 thì 1hour hết hạn token
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Objects> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * Long.parseLong(expiryDay))))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // lấy và giải mã secretKey
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private  <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extracAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extracAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }
}
