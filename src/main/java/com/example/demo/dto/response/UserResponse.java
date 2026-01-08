package com.example.demo.dto.response;

import com.example.demo.model.Address;
import com.example.demo.util.Gender;
import com.example.demo.util.PhoneNumber;
import com.example.demo.util.UserStatus;
import com.example.demo.util.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Builder
@Setter
@Getter
public class UserResponse {
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
