package com.example.demo.service.impl;

import com.example.demo.dto.response.PageResponse;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;

    @Override
    public PageResponse<?> getRolesByUserId(Long userId) {
        List<Role> roles = roleRepository.getAllByUserId(userId);
        List<String> roleNames = new ArrayList<>();
        for(Role role : roles) {
            roleNames.add(role.getName());
        }

        return PageResponse.builder()
                .items(roleNames)
                .build();
    }
}
