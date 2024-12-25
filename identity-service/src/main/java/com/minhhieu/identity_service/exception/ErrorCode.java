package com.minhhieu.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrorCode {

    INVALID_KEY(-1,"Invalid key", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1004, "Username not found", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD(400, "Wrong password", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001,"USER already existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002,"Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003,"Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1005,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006,"You do not have permission", HttpStatus.FORBIDDEN),;



    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

}
