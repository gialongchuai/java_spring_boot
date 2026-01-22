package com.example.demo.service.impl;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.request.EmailForgotPasswordDTO;
import com.example.demo.dto.request.ResetPasswordRequestDTO;
import com.example.demo.dto.request.SignInRequestDTO;
import com.example.demo.dto.request.TokenResetPasswordDTO;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Token;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.JwtService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        // authen này là 1 câu truy vấn
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        // làm thêm 1 câu truy vấn nữa
        var user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(()
                -> new ResourceNotFoundException(Translator.toLocale("username.password.incorrect")));

        // Token access
        String accessToken = jwtService.generateAccessToken(user);

        // Token refresh
        String refreshToken = jwtService.generateRefreshToken(user);

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
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request) {
        // người dùng gửi lại cái refreshToken có time lâu hơn access yêu cầu accessToken mới do accessToken ít time nên hết hạn
        // validate
        final String refreshToken = request.getHeader(AUTHORIZATION);
        if(StringUtils.isBlank(refreshToken)) {
            throw new ResourceNotFoundException("Token must be not blank!");
        }
        log.info("Token: {}", refreshToken);

        // gọi lấy username trong refreshToken
        final String username = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        log.info("Username: {}", username);

        // truy vấn db xem username đó ổn không
        Optional<User> user = userRepository.findByUsername(username);
        log.info("User: {}", user.get().getId());
        if(!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN,user.get())) { // nếu không quăng lỗi
            throw new ResourceNotFoundException("Token is invalid!");
        } // ô cê thì tạo mới access mới
        String accessToken = jwtService.generateAccessToken(user.get());

        // set lại access mới và refresh vẫn là cũ
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.get().getId())
                .build();
    }

    @Override
    public String logout(HttpServletRequest request) {
        final String accessToken = request.getHeader(AUTHORIZATION);
        if(StringUtils.isBlank(accessToken)) {
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
        if(!user.isEnabled()) {{
            throw new ResourceNotFoundException("User is inactive");
        }}

        String resetToken = jwtService.generateResetToken(user);
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
        Optional<User> user = userRepository.findByUsername(username);
        if(!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, user.get())) {
            throw new ResourceNotFoundException("Token is invalid!");
        }
        return "Reset!";
    }

    @Override
    public String changePassword(ResetPasswordRequestDTO requestDTO) {
        User user = isValidToken(requestDTO.getSecretKey());

        if(!requestDTO.getNewPassword().equals(requestDTO.getConfirmNewPassword())) {
            throw new ResourceNotFoundException("Confirm Password is false!");
        }
        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        userRepository.save(user);

        return "Change password successfully!";
    }

    private User isValidToken(String secretKey) {
        final String username = jwtService.extractUsername(secretKey, TokenType.RESET_TOKEN);
        User user = userRepository.findByUsernameEntity(username);

        if(!user.isEnabled()) {{
            throw new ResourceNotFoundException("User is inactive");
        }}
        if(!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, user)) {
            throw new ResourceNotFoundException("Token is invalid!");
        }

        return user;
    }
}
