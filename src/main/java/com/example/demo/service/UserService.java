package com.example.demo.service;

import com.example.demo.dto.request.UserRequestDTO;

public interface UserService {
    int addUser(UserRequestDTO userRequestDTO);
    UserRequestDTO getUser(int userId);
}
