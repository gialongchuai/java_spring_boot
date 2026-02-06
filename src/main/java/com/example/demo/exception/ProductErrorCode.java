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
public enum ProductErrorCode implements BaseErrorCode {
    PRODUCT_IS_REQUIRED(3001, "Product is required!", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(3002, "Product not exist!", HttpStatus.NOT_FOUND),
    TIME_CREATE_IS_REQUIRED(3003, "Time create product is required!", HttpStatus.BAD_REQUEST),
    TIME_UPDATE_IS_REQUIRED(3004, "Time update product is required!", HttpStatus.BAD_REQUEST),
    THUMBNAIL_IS_REQUIRED(3005, "Thumbnail is required!", HttpStatus.BAD_REQUEST),
    IMAGE_IS_REQUIRED(3006, "Image is required!", HttpStatus.BAD_REQUEST),
    PRICE_IS_REQUIRED(3007, "Price is required!", HttpStatus.BAD_REQUEST),
    PRICE_INVALID(3008, "Price must be greater than or equal to 0!", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
