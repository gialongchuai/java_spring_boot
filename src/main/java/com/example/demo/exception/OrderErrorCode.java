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
public enum OrderErrorCode implements BaseErrorCode {
    ORDER_IS_REQUIRED(5001, "Order is required!", HttpStatus.BAD_REQUEST),
    USER_ORDER_NOT_EXISTED(5002, "User orders not exist!", HttpStatus.NOT_FOUND),
    ORDER_NOT_EXISTED(5003, "Order not exist!", HttpStatus.NOT_FOUND),
    ORDER_NAME_IS_REQUIRED(5004, "Order name is required!", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_IS_REQUIRED(5005, "Phone number is required!", HttpStatus.BAD_REQUEST),
    ADDRESS_IS_REQUIRED(5006, "Address is required!", HttpStatus.BAD_REQUEST),
    TOTAL_MONEY_IS_REQUIRED(5007, "Total money is required!", HttpStatus.BAD_REQUEST),
    TOTAL_MONEY_INVALID(5008, "Total money must be 0 or greater!", HttpStatus.BAD_REQUEST),
    STATUS_IS_REQUIRED(5009, "Status is required!", HttpStatus.BAD_REQUEST),
    USER_IS_REQUIRED(5010, "User is required!", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
