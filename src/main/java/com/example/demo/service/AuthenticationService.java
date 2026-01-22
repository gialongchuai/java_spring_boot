package com.example.demo.service;

import com.example.demo.dto.request.EmailForgotPasswordDTO;
import com.example.demo.dto.request.ResetPasswordRequestDTO;
import com.example.demo.dto.request.SignInRequestDTO;
import com.example.demo.dto.request.TokenResetPasswordDTO;
import com.example.demo.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    TokenResponse authenticate(SignInRequestDTO signInRequest);
    TokenResponse refresh(HttpServletRequest request);
    String logout(HttpServletRequest request);

    String forgotPassword(EmailForgotPasswordDTO request);
    String resetPassword(TokenResetPasswordDTO tokenResetPassword);
    String changePassword(ResetPasswordRequestDTO requestDTO);
}
