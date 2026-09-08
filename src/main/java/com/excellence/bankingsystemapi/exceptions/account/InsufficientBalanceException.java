package com.excellence.bankingsystemapi.exceptions.account;

import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;

/**
 * Thrown when an account
 * does not have enough funds
 * to complete an operation.
 */
public class InsufficientBalanceException extends BusinessException {

    public InsufficientBalanceException(String message) {
        super(message, ErrorCode.INSUFFICIENT_BALANCE);
    }
}