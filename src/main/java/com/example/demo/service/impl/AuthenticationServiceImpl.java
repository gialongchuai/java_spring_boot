package com.example.demo.service.impl;

import com.example.demo.dto.request.EmailForgotPasswordDTO;
import com.example.demo.dto.request.ResetPasswordRequestDTO;
import com.example.demo.dto.request.SignInRequestDTO;
import com.example.demo.dto.request.TokenResetPasswordDTO;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.Token;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.TokenService;
import com.example.demo.util.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtServiceImpl jwtService;
    TokenService tokenService;
    PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse authenticate(SignInRequestDTO signInRequest) {

        try {
            Set<String> authorities = new HashSet<>();
            // authen này là 1 câu truy vấn
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
            authentication.getAuthorities().forEach(authority
                    -> authorities.add(authority.getAuthority()));

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

            // Không cần thiết bởi vì đâu còn dùng nữa đâu mà set, có giải thích trong readme
//            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Token access
            String accessToken = jwtService.generateAccessToken(user.getId(), user.getUsername(), authorities);

            // Token refresh
            String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getUsername(), authorities);

            // save token to db
            tokenService.save(Token.builder()
                    .username(user.getUsername())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build());

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request) {
        // người dùng gửi lại cái refreshToken có time lâu hơn access yêu cầu accessToken mới do accessToken ít time nên hết hạn
        // validate
        final String refreshToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.isBlank(refreshToken)) {
            throw new ResourceNotFoundException("Token must be not blank!");
        }
        log.info("Token: {}", refreshToken);

        // gọi lấy username trong refreshToken
        final String username = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        log.info("Username: {}", username);

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new ResourceNotFoundException("User not found!");
        });

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        log.info("User: {}", user.getId());
        if (!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, customUserDetails)) { // nếu không quăng lỗi
            throw new ResourceNotFoundException("Token is invalid!");
        } // ô cê thì tạo mới access mới

        Set<String> authorities = new HashSet<>();
        customUserDetails.getAuthorities().forEach(authority
                -> authorities.add(authority.getAuthority()));
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getUsername(), authorities);

        // set lại access mới và refresh vẫn là cũ
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public String logout(HttpServletRequest request) {
        final String accessToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.isBlank(accessToken)) {
            throw new ResourceNotFoundException("Token must be not blank!");
        }

        // Token kèm header phải là access thì mới cho logout
        final String username = jwtService.extractUsername(accessToken, TokenType.ACCESS_TOKEN);
        Token tokenByUsername = tokenService.getTokenByUsername(username);
        tokenService.delete(tokenByUsername);

        return "Deleted!";
    }

    // ======================= FORGOT PASSWORD =======================
    @Override
    public String forgotPassword(EmailForgotPasswordDTO request) {
        User user = userRepository.findByEmail(request.getEmail());

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        if (!customUserDetails.isEnabled()) {
            throw new ResourceNotFoundException("User is inactive");
        }

        Set<String> authorities = new HashSet<>();
        customUserDetails.getAuthorities().forEach(authority
                -> authorities.add(authority.getAuthority()));

        String resetToken = jwtService.generateResetToken(user.getId(), user.getUsername(), authorities);
        String formConfirmEmail = String.format("curl --location 'http://localhost:8080/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--header 'Accept-Language: vi-VN' \\\n" +
                "--data '{\n" +
                "    \"token\": \"%s\"\n" +
                "}'", resetToken);
        System.out.println(formConfirmEmail);
        return "Sent!";
    }

    @Override
    public String resetPassword(TokenResetPasswordDTO tokenResetPassword) {
        final String secretKey = tokenResetPassword.getToken();
        final String username = jwtService.extractUsername(secretKey, TokenType.RESET_TOKEN);

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new ResourceNotFoundException("User not found!");
        });

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        if (!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, customUserDetails)) {
            throw new ResourceNotFoundException("Token is invalid!");
        }
        return "Reset!";
    }

    @Override
    public String changePassword(ResetPasswordRequestDTO requestDTO) {
        User user = isValidToken(requestDTO.getSecretKey());

        if (!requestDTO.getNewPassword().equals(requestDTO.getConfirmNewPassword())) {
            throw new ResourceNotFoundException("Confirm Password is false!");
        }
        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        userRepository.save(user);

        return "Change password successfully!";
    }

    private User isValidToken(String secretKey) {
        final String username = jwtService.extractUsername(secretKey, TokenType.RESET_TOKEN);
        User user = userRepository.findByUsernameEntity(username);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        if (!customUserDetails.isEnabled()) {
            throw new ResourceNotFoundException("User is inactive");
        }
        if (!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, customUserDetails)) {
            throw new ResourceNotFoundException("Token is invalid!");
        }

        return user;
    }
}
