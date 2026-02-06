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
public enum UploadFileErrorCode implements BaseErrorCode {
    FILE_TOO_LARGE(4001, "File size exceeds the maximum allowed limit of 10MB!", HttpStatus.BAD_REQUEST),
    INVALID_FILE_FORMAT(4002, "Unsupported file type. Only JPEG and PNG are allowed!", HttpStatus.BAD_REQUEST),
    UPLOAD_FAILED(4003, "Failed upload file!", HttpStatus.BAD_REQUEST),
    EMPTY_FILE(4004, "File is empty!", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
