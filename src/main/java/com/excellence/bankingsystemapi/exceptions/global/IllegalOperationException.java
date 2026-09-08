package com.excellence.bankingsystemapi.exceptions.global;

import com.excellence.bankingsystemapi.exceptions.account.BusinessException;

/**
 * Thrown when an operation violates
 * a business rule.
 *
 * Examples:
 * - Closing an account with funds
 * - Reopening a closed account
 * - Deleting a protected resource
 */
public class IllegalOperationException extends BusinessException {

    public IllegalOperationException(String message) {
        super(message, ErrorCode.ILLEGAL_OPERATION);
    }
}