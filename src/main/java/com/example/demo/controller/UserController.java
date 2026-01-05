package com.example.demo.controller;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.response.ResponseData;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/")
    public ResponseData<Integer> addUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        System.out.println(requestDTO);
        return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully!", 1);
    }

    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable int userId, @RequestBody UserRequestDTO requestDTO) {
        System.out.println("Dang update userID: " + userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated!");
    }

    @PatchMapping("/{userId}")
    public ResponseData<?> changeUserStatus(@PathVariable int userId, @RequestParam boolean status) {
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User status changed!");
    }

    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable int userId) {
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted!");
    }

    @GetMapping("/{userId}")
    public ResponseData<UserRequestDTO> getUser(@PathVariable int userId) {
        return new ResponseData<>(HttpStatus.OK.value(), "user", new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0321123123"));
    }

    @GetMapping("/list")
    public ResponseData<List<UserRequestDTO>> getUserList() {
        return new ResponseData<>(HttpStatus.OK.value(), "list user", List.of(new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0321123123"),
                new UserRequestDTO("Tay", "Java", "abc@gmail.com", "0321123123")));
    }
}
