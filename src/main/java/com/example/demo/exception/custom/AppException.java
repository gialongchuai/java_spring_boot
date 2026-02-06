package com.example.demo.exception.custom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppException extends RuntimeException {
    BaseErrorCode baseErrorCode;

    public AppException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode.getMessage());
        this.baseErrorCode = baseErrorCode;
    }
}
