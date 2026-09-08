package com.excellence.bankingsystemapi.exceptions.global;

import com.excellence.bankingsystemapi.exceptions.account.BusinessException;

/**
 * Thrown when a request is invalid
 * or violates request rules.
 */
public class InvalidRequestException extends BusinessException {

    public InvalidRequestException(String message) {
        super(message, ErrorCode.INVALID_REQUEST);
    }
}