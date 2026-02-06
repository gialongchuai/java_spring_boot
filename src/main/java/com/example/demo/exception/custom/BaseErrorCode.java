package com.example.demo.exception.custom;

import org.springframework.http.HttpStatusCode;

public interface BaseErrorCode {
    int getCode();

    String getMessage();

    HttpStatusCode getHttpStatusCode();
}
