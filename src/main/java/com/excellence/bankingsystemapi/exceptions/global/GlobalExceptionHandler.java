package com.excellence.bankingsystemapi.exceptions.global;

import com.excellence.bankingsystemapi.common.ApiResponse;
import com.excellence.bankingsystemapi.common.ApiResponseFactory;
import com.excellence.bankingsystemapi.exceptions.account.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * ===========================================================
     * 404 - Resource Not Found
     * ===========================================================
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {


        log.warn("Resource not found: {}", ex.getMessage());


        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponseFactory.error(ex.getMessage(),
                        HttpStatus.NOT_FOUND,
                        request,
                        ex.getErrorCode()));
    }


    /**
     * ===========================================================
     * 409 - Business Conflict
     * ===========================================================
     */
    @ExceptionHandler({DuplicateResourceException.class, DuplicateAccountException.class,

            AccountAlreadyClosedException.class, AccountFrozenException.class, AccountNotActiveException.class, AccountNotFrozenException.class, AccountNotDormantException.class,

            AccountHasRemainingBalanceException.class,

            InsufficientBalanceException.class,

            CustomerInactiveException.class,

            IllegalOperationException.class})
    public ResponseEntity<ApiResponse<Void>> handleBusinessConflict(
            BusinessException ex,
            HttpServletRequest request) {


        log.warn("Business conflict: {}", ex.getMessage());


        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponseFactory.error(ex.getMessage(),
                        HttpStatus.CONFLICT,
                        request,
                        ex.getErrorCode()));
    }


    /**
     * ===========================================================
     * 403 - Unauthorized Operation
     * ===========================================================
     */
    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedOperation(
            UnauthorizedOperationException ex,
            HttpServletRequest request) {


        log.warn("Unauthorized operation: {}", ex.getMessage());


        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponseFactory.error(ex.getMessage(),
                        HttpStatus.FORBIDDEN,
                        request,
                        ex.getErrorCode()));
    }


    /**
     * ===========================================================
     * 503 - External Service Failure
     * ===========================================================
     */
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiResponse<Void>> handleExternalService(
            ExternalServiceException ex,
            HttpServletRequest request) {


        log.error("External service failure: {}", ex.getMessage());


        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponseFactory.error(ex.getMessage(),
                        HttpStatus.SERVICE_UNAVAILABLE,
                        request,
                        ex.getErrorCode()));
    }


    /**
     * ===========================================================
     * 400 - Validation Errors
     * ===========================================================
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {


        Map<String, String> errors = new LinkedHashMap<>();


        for (FieldError error : ex.getBindingResult().getFieldErrors()) {


            errors.putIfAbsent(error.getField(), error.getDefaultMessage());
        }


        return ResponseEntity
                .badRequest()
                .body(ApiResponseFactory.error("Validation failed.",
                        errors,
                        HttpStatus.BAD_REQUEST,
                        request,
                        ErrorCode.VALIDATION_FAILED));
    }


    /**
     * ===========================================================
     * 400 - Constraint Validation
     * ===========================================================
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {


        return ResponseEntity
                .badRequest()
                .body(ApiResponseFactory.error(ex.getMessage(),
                        HttpStatus.BAD_REQUEST,
                        request,
                        ErrorCode.VALIDATION_FAILED));
    }


    /**
     * ===========================================================
     * 400 - Invalid Request Body
     * ===========================================================
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRequestBody(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {


        return ResponseEntity
                .badRequest()
                .body(ApiResponseFactory.error("Malformed request body.",
                        HttpStatus.BAD_REQUEST,
                        request,
                        ErrorCode.INVALID_REQUEST));
    }


    /**
     * ===========================================================
     * 400 - Invalid Parameter Type
     * ===========================================================
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {


        return ResponseEntity
                .badRequest()
                .body(ApiResponseFactory.error(
                        "Invalid value supplied for parameter: " + ex.getName(),
                        HttpStatus.BAD_REQUEST,
                        request,
                        ErrorCode.INVALID_REQUEST));
    }


    /**
     * ===========================================================
     * 400 - Generic Business Rule Failure
     * ===========================================================
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericBusinessException(
            BusinessException ex,
            HttpServletRequest request) {


        log.warn("Business exception: {}", ex.getMessage());


        return ResponseEntity
                .badRequest()
                .body(ApiResponseFactory.error(ex.getMessage(),
                        HttpStatus.BAD_REQUEST,
                        request,
                        ex.getErrorCode()));
    }


    /**
     * ===========================================================
     * 500 - Unexpected System Errors
     * ===========================================================
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception ex,
            HttpServletRequest request) {


        log.error("Unexpected server error", ex);


        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseFactory.error("An unexpected error occurred.",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        request,
                        ErrorCode.INTERNAL_SERVER_ERROR));
    }

}