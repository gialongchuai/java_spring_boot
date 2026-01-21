package com.example.demo.service;

import com.example.demo.dto.request.SignInRequest;
import com.example.demo.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    TokenResponse authenticate(SignInRequest signInRequest);

    TokenResponse refresh(HttpServletRequest request);

    String logout(HttpServletRequest request);
}
