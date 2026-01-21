package com.example.demo.controller;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.request.SignInRequest;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.ResponseError;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.JwtService;
import com.example.demo.service.impl.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Validated
public class AuthenticationController {
    AuthenticationServiceImpl authenticationService;

    // user login sẽ cấp cho 2 cái : accesstoken + refreshtoken
    // dựa vòa yml build cho type token này ACCESS và REFRESH
    // phân loại goọi tới api phải dùng ACCTOKEN đc cấp
    // chứ không được dùng REFRESH
    @Operation(summary = "user login", description = "API login user!")
    @PostMapping("/access")
    public ResponseData<TokenResponse> login(@RequestBody SignInRequest signInRequest) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("auth.login.authenticated"), authenticationService.authenticate(signInRequest));
        } catch (Exception e) {
            log.error("Username or Password is incorrect!: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    // dùng refreshtoken để yêu cầu cấp accesstoken mới do accesstoken có time ít hơn refresh
    // và bị hết hạn và tất nhiên không thể dùng type ACCESSTOKEN y/c cấp access rồi !!! (type)
    // access mới và refresh vẫn như cũ gửi thông qua header !!!
    @Operation(summary = "user refresh token", description = "API for user refresh token!")
    @PostMapping("/refresh")
    public ResponseData<TokenResponse> refresh(HttpServletRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("auth.token.refresh.success"), authenticationService.refresh(request));
        } catch (Exception e) {
            log.error("Loi roi!: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    // khi người dùng muốn logout sẽ gửi kèm accessToken vào request y/c xóa token khỏi db Token
    @Operation(summary = "user logout", description = "API for user logout!")
    @PostMapping("/logout")
    public ResponseData<String> logout(HttpServletRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("auth.logout.success"), authenticationService.logout(request));
        } catch (Exception e) {
            log.error("Loi roi!: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }
}
