package com.example.demo.service;

import com.example.demo.dto.response.PageResponse;

public interface RoleService {
    PageResponse<?> getRolesByUserId(Long userId);
}
