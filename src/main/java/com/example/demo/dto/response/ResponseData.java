package com.example.demo.dto.response;

public class ResponseData<T> {
    private int status;
    private String message;
    private T data;

    // Put patch delete
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // get post
    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
