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
public enum CategoryErrorCode implements BaseErrorCode {
    CATEGORY_IS_REQUIRED(2001, "Category is required!", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(2002, "Category not exist!", HttpStatus.NOT_FOUND);

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
