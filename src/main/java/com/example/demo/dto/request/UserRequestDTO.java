package com.example.demo.dto.request;

import com.example.demo.util.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.xml.crypto.Data;

public class UserRequestDTO {
    @NotBlank(message = "firstName must not be blank!")
    private String firstName;

    @NotNull(message = "lastName must not be null!")
    private String lastName;

    @Email
    private String email;

    @PhoneNumber
    private String phone;
    private Data dateOfBirth;



    public UserRequestDTO(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
