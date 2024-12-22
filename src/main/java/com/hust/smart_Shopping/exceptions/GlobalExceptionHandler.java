package com.hust.smart_Shopping.exceptions;

import java.time.Instant;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hust.smart_Shopping.dtos.ApiResponse;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.exceptions.payload.ExpiredTokenException;
import com.hust.smart_Shopping.exceptions.payload.VerificationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception ex) {
        log.error("Exception: ", ex);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setMessage(String.valueOf(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode()));
        apiResponse.setError(ex.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setMessage(String.valueOf(errorCode.getCode()));
        apiResponse.setError(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler({ AuthenticationException.class, VerificationException.class, ExpiredTokenException.class })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse> authenticationException(AuthenticationException ex) {
        log.error("Exception: ", ex);
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setError(ex.getMessage());
        apiResponse.setMessage(String.valueOf(ErrorCode.UNAUTHORIZED.getCode()));
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse> resourceNotFoundException(DataNotFoundException ex) {
        log.error("Exception: ", ex);
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setError(ex.getMessage());
        apiResponse.setMessage(String.valueOf(ErrorCode.NOT_FOUND.getCode()));
        return ResponseEntity.badRequest().body(apiResponse);
    }

}
