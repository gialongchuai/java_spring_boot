package com.example.demo.service;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.util.UserStatus;

import java.util.List;

public interface UserService {
    Long saveUser(UserRequestDTO requestDTO);
    void updateUser(Long userId, UserRequestDTO requestDTO);
    void changeStatusUser(Long userId, UserStatus userStatus);
    void deleteUser(Long userId);
    UserResponse getUser(Long userId);
    PageResponse<?> getAllUsers(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getAllUsersOrderWithMultipleColumns(int pageNo, int pageSize, String... sorts);
}
