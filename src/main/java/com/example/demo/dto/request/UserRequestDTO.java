package com.example.demo.dto.request;

import com.example.demo.util.Gender;
import com.example.demo.util.PhoneNumber;
import com.example.demo.util.UserStatus;
import com.example.demo.util.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@ToString
public class UserRequestDTO {
//    @NotBlank(message = "firstName must not be blank!")
    private String firstName;

//    @NotNull(message = "lastName must not be null!")
    private String lastName;

    @Email
    private String email;

//    @PhoneNumber
    private String phone;
    private Date dateOfBirth;

    private Gender gender;

    private String username;

    private String password;

    private UserType type;

    private UserStatus status;

    private List<AddressRequestDTO> addresses;
}
