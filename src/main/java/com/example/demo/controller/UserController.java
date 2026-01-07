package com.example.demo.controller;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Add user", description = "API create a user!")
    @PostMapping("/")
    public ResponseData<Integer> addUser(@Valid @RequestBody UserRequestDTO requestDTO) {
//        System.out.println(requestDTO);
        return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), userService.addUser(requestDTO));
    }

    @Operation(summary = "Update user", description = "API update a user!")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable int userId, @RequestBody UserRequestDTO requestDTO) {
        System.out.println("Dang update userID: " + userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"));
    }

    @Operation(summary = "Update status user", description = "API update status a user!")
    @PatchMapping("/{userId}")
    public ResponseData<?> changeUserStatus(@PathVariable int userId, @RequestParam boolean status) {
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.update.success"));
    }

    @Operation(summary = "Delete a user", description = "API delete a user!")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable int userId) {
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocale("user.delete.success"));
    }

    @Operation(summary = "Get information a user", description = "API get a user!")
    @GetMapping("/{userId}")
    public ResponseData<UserRequestDTO> getUser(@PathVariable @Min(1) @Max(10) @Valid int userId) { // new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0321123123")
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.get.success"), userService.getUser(userId));
    }

    @Operation(summary = "Get information a user list", description = "API get list user!")
    @GetMapping("/list")
    public ResponseData<List<UserRequestDTO>> getUserList() {
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.get.success"), List.of(new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0321123123"),
                new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0321123123")));
    }
}
