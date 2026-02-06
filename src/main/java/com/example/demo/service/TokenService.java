package com.example.demo.service;

import com.example.demo.configuration.Translator;
import com.example.demo.exception.custom.ResourceNotFoundException;
import com.example.demo.model.Token;
import com.example.demo.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record TokenService(TokenRepository tokenRepository) {
    public Long save(Token token) {
        Optional<Token> optionalToken = tokenRepository.findByUsername(token.getUsername());
        if (optionalToken.isEmpty()) { // nếu chưa có token thì lưu mới
            tokenRepository.save(token);
            return token.getId();
        } else { // nếu có thì update
            Token currentToken = optionalToken.get();
            currentToken.setAccessToken(token.getAccessToken());
            currentToken.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(currentToken);
            return currentToken.getId();
        }
    }

    public String delete(Token token) {
        tokenRepository.delete(token);
        return "Deleted!";
    }

    public Token getTokenByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("auth.token.user.not_found")));
    }
}
