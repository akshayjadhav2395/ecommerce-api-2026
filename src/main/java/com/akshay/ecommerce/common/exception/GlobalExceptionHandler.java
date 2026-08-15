package com.akshay.ecommerce.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                false,
                exception.getMessage(),
                null);

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> resourceAlreadyExistsException(ResourceAlreadyExistsException exception) {

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                false,
                exception.getMessage(),
                null);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException exception) {

        Map<String, String> map = new HashMap<>();

        for( FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> globalExceptionHandler(Exception exception) throws Exception {

        // Let Spring Security handle authorization exceptions
        if (exception instanceof AuthorizationDeniedException) {
            throw exception;
        }

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                false,
                "Something went wrong. Please contact support.",
                null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> badCredentialsExceptionHandler(BadCredentialsException exception) {

        ApiResponse response = new ApiResponse(
                false,
                exception.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}

