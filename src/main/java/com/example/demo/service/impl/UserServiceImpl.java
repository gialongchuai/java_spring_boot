package com.example.demo.service.impl;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public int addUser(UserRequestDTO userRequestDTO) {
        System.out.println("=== Universe! ===");
        return 1;
    }

    @Override
    public UserRequestDTO getUser(int userId) {
        return new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0123456789");
    }
}
