package com.excellence.bankingsystemapi.exceptions.account;

import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;

/**
 * Thrown when an operation is attempted
 * on a closed account.
 */
public class AccountAlreadyClosedException extends BusinessException {

    public AccountAlreadyClosedException(String message) {
        super(message, ErrorCode.ACCOUNT_ALREADY_CLOSED);
    }
}