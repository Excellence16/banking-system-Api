package com.excellence.bankingsystemapi.exceptions.global;

/**
 * Standard application error codes.
 *
 * Used to provide stable, machine-readable
 * error responses to API clients.
 */
public enum ErrorCode {

    // Validation
    VALIDATION_FAILED,

    // Resources
    RESOURCE_NOT_FOUND,
    DUPLICATE_RESOURCE,

    // Requests
    INVALID_REQUEST,

    // Business Rules
    BUSINESS_RULE_VIOLATION,
    ILLEGAL_OPERATION,

    // Account
    DUPLICATE_ACCOUNT,
    ACCOUNT_ALREADY_CLOSED,
    ACCOUNT_ALREADY_FROZEN,
    ACCOUNT_NOT_ACTIVE,
    ACCOUNT_NOT_FROZEN,
    ACCOUNT_NOT_DORMANT,
    ACCOUNT_DORMANT,
    INSUFFICIENT_BALANCE,

    // Customer
    CUSTOMER_NOT_ACTIVE,

    // Security
    UNAUTHORIZED,
    FORBIDDEN,

    // External Services
    EXTERNAL_SERVICE_ERROR,

    // System
    INTERNAL_SERVER_ERROR
}