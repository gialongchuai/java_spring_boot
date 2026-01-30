package com.example.demo.exception;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(e.getFieldError().getDefaultMessage());

        return errorResponse;
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        String shortMessage = e.getMessage().substring(e.getMessage().indexOf(":")+1);
        errorResponse.setMessage(shortMessage);

        return errorResponse;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

        String paramName = e.getName(); // ví dụ pageSize, pageNo gì đó nếu không đúng định dạng
        String mess = Translator.toLocale("error.param.type.mismatch"); // có %s thì
        String finalMess = String.format(mess, paramName); // dùng String format gán vào

        errorResponse.setMessage(finalMess);

        return errorResponse;
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());

        return errorResponse;
    }
}
