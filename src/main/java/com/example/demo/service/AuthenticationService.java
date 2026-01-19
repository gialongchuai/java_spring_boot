package com.example.demo.service;

import com.example.demo.dto.request.SignInRequest;
import com.example.demo.dto.response.TokenResponse;

public interface AuthenticationService {
    public TokenResponse authenticate(SignInRequest signInRequest);
}
