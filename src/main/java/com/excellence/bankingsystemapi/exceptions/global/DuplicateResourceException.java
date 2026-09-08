package com.excellence.bankingsystemapi.exceptions.global;

import com.excellence.bankingsystemapi.exceptions.account.BusinessException;

/**
 * Thrown when attempting to create
 * a resource that already exists.
 *
 * Examples:
 * - Email
 * - BVN
 * - Username
 * - Account Number
 */
public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String message) {
        super(message, ErrorCode.DUPLICATE_RESOURCE);
    }
}