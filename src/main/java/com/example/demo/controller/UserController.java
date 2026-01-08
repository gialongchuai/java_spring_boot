package com.example.demo.controller;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.ResponseError;
import com.example.demo.dto.response.UserResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class UserController {
    UserService userService;

    @Operation(summary = "Add user", description = "API create a user!")
    @PostMapping("/")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO requestDTO) {
//        System.out.println(requestDTO);
        try {
            return new ResponseData<>(HttpStatus.CREATED.value()
                    , Translator.toLocale("user.add.success")
                    , userService.saveUser(requestDTO));
        } catch (Exception e) {
            log.error("Error added user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), Translator.toLocale("user.add.fail"));
        }
    }

    @Operation(summary = "Update user", description = "API update a user!")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable Long userId, @RequestBody UserRequestDTO requestDTO) {
        System.out.println("Dang update userID: " + userId);
        userService.updateUser(userId, requestDTO);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"));
    }

    @Operation(summary = "Update status user", description = "API update status a user!")
    @PatchMapping("/{userId}")
    public ResponseData<?> changeUserStatus(@PathVariable Long userId, @RequestParam UserStatus status) {
        userService.changeStatusUser(userId, status);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"));
    }

    @Operation(summary = "Delete a user", description = "API delete a user!")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocale("user.delete.success"));
    }

    @Operation(summary = "Get information a user", description = "API get a user!")
    @GetMapping("/{userId}")
    public ResponseData<UserResponse> getUser(@PathVariable @Min(1) @Valid Long userId) { // new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0321123123")
        return new ResponseData(HttpStatus.OK.value(), Translator.toLocale("user.get.success"), userService.getUser(userId));
//        return null;
    }

    @Operation(summary = "Get information a user list", description = "API get list user!")
    @GetMapping("/list")
    public ResponseData<List<UserRequestDTO>> getUserList() {
//        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.get.success"), List.of(new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0321123123"),
//                new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0321123123")));
        return null;
    }
}
