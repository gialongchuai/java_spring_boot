package com.example.demo.service.impl;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.request.SignInRequest;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    @Override
    public TokenResponse authenticate(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        var user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(()
                -> new ResourceNotFoundException(Translator.toLocale("username.password.incorrect")));

        // Token access
        String access_token = jwtService.generateToken(user);
        return TokenResponse.builder()
                .accessToken(access_token)
                .refreshToken("refresh-token")
                .userId(user.getId())
                .build();
    }
}
