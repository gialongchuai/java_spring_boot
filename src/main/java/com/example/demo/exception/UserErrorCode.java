package com.example.demo.exception;

import com.example.demo.exception.custom.BaseErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum UserErrorCode implements BaseErrorCode {
    USER_EXISTED(1001, "User existed!", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "User not exist!", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1003, "Username must be at least {min} characters!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} characters!", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1005, "Your age must be at least {min}!", HttpStatus.BAD_REQUEST),
    CANNOT_SEND_EMAIL(1006, "Cannot send email!", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_REQUIRED(1007, "Phone number is required!", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1008, "Password is required!", HttpStatus.BAD_REQUEST),
    ROLE_USER_NOT_EXISTED(1009, "Please check role in the system!", HttpStatus.BAD_REQUEST),
    USER_IS_LOCKED(1010, "User is locked!", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
