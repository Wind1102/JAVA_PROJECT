package com.minhhieu.identity_service.exception;

import com.minhhieu.identity_service.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice()
public class GlobalExceptionHandler {
    private final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlingRuntimeException(Exception ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ApiResponse>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getBindingResult().getFieldErrors().stream().map(error -> {
            String enumKey = error.getDefaultMessage();
            ErrorCode errorCode = ErrorCode.INVALID_KEY;
            Map<String,Object> attributes = null;
            try {
                errorCode = ErrorCode.valueOf(enumKey);
                var constraintViolations = ex.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

                attributes = constraintViolations.getConstraintDescriptor().getAttributes();

            } catch (IllegalArgumentException e) {

            }
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setCode(errorCode.getCode());
            apiResponse.setMessage(Objects.nonNull(attributes)?mapAttribute(errorCode.getMessage(), attributes):errorCode.getMessage());
            return apiResponse;
        }).collect(Collectors.toList()));
    }

    private String mapAttribute(String message, Map<String,Object> attributes ){
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

}