package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;

    // nghe nói là gửi ngầm để không y/c người dùng login lại
    // mà vẫn duy trì cái token
    private String refreshToken;

    private Long userId;
}
