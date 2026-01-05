package com.example.demo.controller;

import com.example.demo.dto.request.UserRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/")
    public String addUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        System.out.println(requestDTO);
        return "User added!";
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable int userId, @RequestBody UserRequestDTO requestDTO) {
        System.out.println("Dang update userID: " + userId);
        return "User updated!";
    }

    @PatchMapping("/{userId}")
    public String changeUserStatus(@PathVariable int userId, @RequestParam boolean status) {
        return "User status changed!";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable int userId) {
        return "User deleted!";
    }

    @GetMapping("/{userId}")
    public UserRequestDTO getUser(@PathVariable int userId) {
        return new UserRequestDTO("Tay", "Java", "abc@gmail.com","0321123123");
    }
}
