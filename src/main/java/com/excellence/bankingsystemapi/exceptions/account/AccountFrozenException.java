package com.excellence.bankingsystemapi.exceptions.account;

import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;

/**
 * Thrown when an operation is attempted
 * on a frozen account.
 */
public class AccountFrozenException extends BusinessException {

    public AccountFrozenException(String message) {
        super(message, ErrorCode.ACCOUNT_ALREADY_FROZEN);
    }
}