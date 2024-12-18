package com.minhhieu.identity_service.exception;

public enum ErrorCode {

    INVALID_KEY(-1,"Invalid key"),
    INVALID_USERNAME(404, "User Exists"),
    WRONG_PASSWORD(400, "Wrong password"),
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error"),
    USER_EXISTED(1001,"USER already existed"),
    USERNAME_INVALID(1002,"Username must be at least 3 characters"),
    PASSWORD_INVALID(1003,"Password must be at least 8 characters");



    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
