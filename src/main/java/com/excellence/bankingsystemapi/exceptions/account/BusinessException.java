package com.excellence.bankingsystemapi.exceptions.account;

import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;
import lombok.Getter;

/**
 * Base class for all business-related exceptions.
 *
 * <p>Every business exception carries:
 * <ul>
 *     <li>A human-readable message.</li>
 *     <li>A machine-readable {@link ErrorCode}.</li>
 * </ul>
 *
 * This allows the GlobalExceptionHandler to build
 * consistent API error responses without hardcoding
 * error codes for every exception type.
 */
@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}