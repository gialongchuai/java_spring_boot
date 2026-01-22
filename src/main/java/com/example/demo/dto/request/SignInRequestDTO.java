package com.example.demo.dto.request;

import com.example.demo.util.Platform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDTO {

    @NotBlank(message = "username must be not blank!")
    private String username;

    @NotBlank(message = "password must be not blank!")
    private String password;

    @NotNull(message = "platform must be not null!")
    private Platform platform; // android, ios, web

    // Mỗi mobile cho api có device cho mobile
    // 4 cái đt khác nhau thì 4 cái notifi khác nhau
    private String deviceToken; //
}
