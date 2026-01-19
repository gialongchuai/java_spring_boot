package com.example.demo.service.impl;

import com.example.demo.service.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String generateToken(UserDetails userDetails) {
        // Todo ...

        return "Dummy-Token";
    }
}
