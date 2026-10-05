package com.excellence.bankingsystemapi.common;

import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

/**
 * Standard API response wrapper for all endpoints.
 *
 * @param <T> Response payload type.
 */
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    // Indicates whether the request was successful.
    boolean success;

    // Human-readable response message.
    String message;

    // HTTP status code.
    int status;

    // Time the response was generated.
    Instant timestamp;

    // Requested endpoint.
    String path;

    // Response payload.
    T data;

    // Present only for error responses.
    ErrorCode errorCode;

}

/*
 * Class Overview
 * --------------------------------------------------------------------
 * - Standard response wrapper for every API response.
 * - Supports both success and error responses.
 * - Generic payload for maximum reusability.
 * - Omits null fields during JSON serialization.
 * - Immutable after creation for thread safety.
 * - Designed for Spring Boot 3 / Java 21 enterprise applications.
 */