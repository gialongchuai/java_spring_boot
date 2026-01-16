package com.example.demo.controller;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.ResponseError;
import com.example.demo.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@Tag(name = "Role Controller")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Validated
public class RoleController {
    RoleService roleService;

    @Operation(summary = "Get all roles of a user by userId", description = "API get all roles of a user by userId!")
    @GetMapping("/{userId}")
    public ResponseData<PageResponse> getRolesByUserId(@PathVariable Long userId) {
        log.info("Chuẩn bị trà trộn get all roles của userId: {}", userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("role.get.success"), roleService.getRolesByUserId(userId));
        } catch (Exception e) {
            log.error("Error get roles: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), Translator.toLocale("role.get.fail"));
        }
    }
}
