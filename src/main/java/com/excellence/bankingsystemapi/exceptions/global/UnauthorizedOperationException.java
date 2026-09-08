package com.excellence.bankingsystemapi.exceptions.global;

import com.excellence.bankingsystemapi.exceptions.account.BusinessException;

/**
 * Thrown when a user attempts
 * to perform an operation
 * they are not permitted to perform.
 *
 * This represents business authorization.
 * Spring Security authorization failures
 * are handled separately.
 */
public class UnauthorizedOperationException extends BusinessException {

    public UnauthorizedOperationException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }
}