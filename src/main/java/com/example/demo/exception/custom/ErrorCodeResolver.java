package com.example.demo.exception.custom;

import java.util.HashMap;
import java.util.Map;

public class ErrorCodeResolver {
    private static final Map<String, BaseErrorCode> ERROR_CODE_MAP = new HashMap<>();

    static {
        // Đăng ký tất cả các enum implements BaseErrorCode vào map (chat)
//        for (SecurityErrorCode errorCode : SecurityErrorCode.values()) {
//            ERROR_CODE_MAP.put(errorCode.name(), errorCode);
//        }
//        for (ProductErrorCode errorCode : ProductErrorCode.values()) {
//            ERROR_CODE_MAP.put(errorCode.name(), errorCode);
//        }
//        for (UploadFileErrorCode errorCode : UploadFileErrorCode.values()) {
//            ERROR_CODE_MAP.put(errorCode.name(), errorCode);
//        }
//        for (UserErrorCode errorCode : UserErrorCode.values()) {
//            ERROR_CODE_MAP.put(errorCode.name(), errorCode);
//        }
//        for (CategoryErrorCode errorCode : CategoryErrorCode.values()) {
//            ERROR_CODE_MAP.put(errorCode.name(), errorCode);
//        }
//        for (OrderErrorCode errorCode : OrderErrorCode.values()) {
//            ERROR_CODE_MAP.put(errorCode.name(), errorCode);
//        }
//        for (OrderDetailErrorCode errorCode : OrderDetailErrorCode.values()) {
//            ERROR_CODE_MAP.put(errorCode.name(), errorCode);
//        }
    }

    public static BaseErrorCode resolve(String enumKey) {
        return ERROR_CODE_MAP.getOrDefault(enumKey, null);
    }
}
