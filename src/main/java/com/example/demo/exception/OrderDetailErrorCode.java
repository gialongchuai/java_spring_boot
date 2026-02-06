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
public enum OrderDetailErrorCode implements BaseErrorCode {
    PRODUCT_NOT_EXISTED(6001, "Product not exist!", HttpStatus.NOT_FOUND),
    ORDER_DETAIL_NOT_EXISTED(6002, "Order detail not exist!", HttpStatus.NOT_FOUND),
    PRICE_IS_REQUIRED(6003, "Price is required!", HttpStatus.BAD_REQUEST),
    PRICE_INVALID(6004, "Price must be greater than or equal to 0!", HttpStatus.BAD_REQUEST),
    NUMBER_OF_PRODUCTS_IS_REQUIRED(6005, "Number of products is required!", HttpStatus.BAD_REQUEST),
    NUMBER_OF_PRODUCTS_INVALID(6006, "Number of products must be at least 1!", HttpStatus.BAD_REQUEST),
    TOTAL_MONEY_IS_REQUIRED(6007, "Total money is required!", HttpStatus.BAD_REQUEST),
    TOTAL_MONEY_INVALID(6008, "Total money must be greater than or equal to 0!", HttpStatus.BAD_REQUEST);


    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
