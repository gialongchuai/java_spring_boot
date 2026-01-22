package com.example.demo.dto.request;

import com.example.demo.model.Address;
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
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
public class ResetPasswordRequestDTO {
    private String secretKey;

    private String newPassword;

    private String confirmNewPassword;
}
