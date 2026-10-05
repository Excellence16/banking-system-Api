package com.excellence.bankingsystemapi.common;


import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.Instant;

/**
 * Owns the ONLY place where an ApiResponse is actually built.
 * success() and error() are the public-facing entry points — thin wrappers.
 * buildResponse() is the private shared engine — nothing outside this class calls it directly.
 */
public class ApiResponseFactory {

    //prevent instantiation
    private ApiResponseFactory() {
        throw new UnsupportedOperationException(
                "Utility class cannot be instantiated.");
        // utility class — never meant to be instantiated
    }


    //Builds a successful API response ..
    public static <T> ApiResponse<T> success(String message,
            T data,
            HttpStatus status,
            HttpServletRequest request) {

        return buildResponse(true, message, status, data, request, null);
    }


    // Error WITH a machine-readable errorCode — this is the one GlobalExceptionHandler
    // should actually call for every known exception type (NotFound, Duplicate, etc.)
    public static <T> ApiResponse<T> error(String message,
            HttpStatus status,
            HttpServletRequest request,
            ErrorCode errorCode) {

        return buildResponse(false, message, status, null, request, errorCode);
    }

    // Same idea, but carries a payload — used for validation errors,
    // where "data" holds the field->message map instead of being null.
    public static <T> ApiResponse<T> error(String message,
            T data,
            HttpStatus status,
            HttpServletRequest request,
            ErrorCode errorCode) {

        return buildResponse(false, message, status, data, request, errorCode);
    }

    private static <T> ApiResponse<T> buildResponse(boolean success,
            String message,
            HttpStatus status,
            T data,
            HttpServletRequest request,
            ErrorCode errorCode) {

        return ApiResponse
                .<T>builder()
                .success(success)
                .message(message)
                .status(status.value())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .data(data)
                .errorCode(errorCode)
                .build();
    }
}