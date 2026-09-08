package com.excellence.bankingsystemapi.exceptions.global;

import com.excellence.bankingsystemapi.exceptions.account.BusinessException;

/**
 * Thrown when a requested resource cannot be found.
 *
 * Examples:
 * - Customer
 * - Account
 * - Transaction
 * - Role
 * - Permission
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(message, ErrorCode.RESOURCE_NOT_FOUND);
    }
}