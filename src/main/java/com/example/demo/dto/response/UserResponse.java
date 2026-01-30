package com.example.demo.dto.response;

import com.example.demo.model.Address;
import com.example.demo.util.Gender;
import com.example.demo.util.UserStatus;
import com.example.demo.util.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Builder
@Setter
@Getter
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date dateOfBirth;
    private Gender gender;
    private String username;
    private String password;
    private UserType type;
    private UserStatus status;
    private Set<Address> addresses = new HashSet<>();
}
