package com.example.demo.controller;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.ResponseError;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.UserService;
import com.example.demo.util.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Validated // Phải để ở ngoài nó mới show ra log lỗi cho requestParam min(10)
public class UserController {
    UserService userService;

    @Operation(summary = "Add user", description = "API create a user!")
    @PostMapping("/")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO requestDTO) {
//        System.out.println("Dang them 1 user: " + requestDTO);
        try {
            return new ResponseData<>(HttpStatus.CREATED.value()
                    , Translator.toLocale("user.add.success")
                    , userService.saveUser(requestDTO));
        } catch (Exception e) {
            log.error("Error add user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), Translator.toLocale("user.add.fail"));
        }
    }

    @Operation(summary = "Update user", description = "API update a user!")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable Long userId, @RequestBody UserRequestDTO requestDTO) {
//        System.out.println("Dang update userID: " + userId);
        try {
            userService.updateUser(userId, requestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"));
        } catch (ResourceNotFoundException e) { // Cái post nếu lỗi thì quăng cái Exception còn cái get do bên service có quăng cái resource nên
            log.error("Error update user: {} {}", e.getMessage(), e.getCause()); // bên này bắt đúng cái lỗi đó luôn !!!
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage()); // đối với lỗi do resource thì quăng lỗi i18n luôn.
        }
    }

    @Operation(summary = "Update status user", description = "API update status a user!")
    @PatchMapping("/{userId}")
    public ResponseData<?> changeUserStatus(@PathVariable Long userId, @RequestParam UserStatus status) {
//        System.out.println("Dang change status cho user: " + userId);
        try {
            userService.changeStatusUser(userId, status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"));
        } catch (ResourceNotFoundException e) {
            log.error("Error change status user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Delete a user", description = "API delete a user!")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable Long userId) {
//        System.out.println("Dang xoa user: " + userId);
        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocale("user.delete.success"));
        } catch (Exception e) {
            log.error("Error delete user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), Translator.toLocale("user.delete.fail"));
        }
    }

    @Operation(summary = "Get information a user", description = "API get a user!")
    @GetMapping("/{userId}")
    public ResponseData<UserResponse> getUser(@PathVariable @Min(1) @Valid Long userId) { // new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0321123123")
//        System.out.println("Dang get user: " + userId);
        try {
            return new ResponseData(HttpStatus.OK.value(), Translator.toLocale("user.get.success"), userService.getUser(userId));
        } catch (ResourceNotFoundException e) {
            log.error("Error get user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get information a user list", description = "API get list user!")
    @GetMapping("/list")
    public ResponseData<PageResponse> getUserList(@RequestParam(defaultValue = "0", required = false) int pageNo
            , @Min(5) @RequestParam(defaultValue = "20", required = false) int pageSize
            , @RequestParam(required = false) String sortBy) {
//        System.out.println("Dang get users voi so trang: " + pageNo + ", so record: " + pageSize);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.get.success"), userService.getAllUsers(pageNo, pageSize, sortBy));
        } catch (Exception e) {
            log.error("Error get users: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), Translator.toLocale("user.get.fail"));
        }
    }

    @Operation(summary = "Get information a user list with multiple columns", description = "API get list user and order list with multiple columns!")
    @GetMapping("/list-order-with-multiple-columns")
    public ResponseData<PageResponse> getUserListOrderWithMultipleColumns(@RequestParam(defaultValue = "0", required = false) int pageNo
            , @Min(5) @RequestParam(defaultValue = "20", required = false) int pageSize
            , @RequestParam(required = false) String... sortBy) {
        System.out.println("Dang get users voi so trang: " + pageNo + ", so record: " + pageSize);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.get.success"), userService.getAllUsersOrderWithMultipleColumns(pageNo, pageSize, sortBy));
        } catch (Exception e) {
            log.error("Error get users: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), Translator.toLocale("user.get.fail"));
        }
    }
}
