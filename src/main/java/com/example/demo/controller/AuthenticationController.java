package com.example.demo.controller;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.request.SignInRequest;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.ResponseError;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.JwtService;
import com.example.demo.service.impl.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @PostMapping("/access")
    public ResponseData<TokenResponse> login(@RequestBody SignInRequest signInRequest) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("auth.login.authenticated"), authenticationService.authenticate(signInRequest));
        } catch (Exception e) {
            log.error("Username or Password is incorrect!: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    @PostMapping("/logout")
    public String logout() {
        return "Success!";
    }

    @PostMapping("/refresh")
    public String refresh() {
        return "Success!";
    }
}
