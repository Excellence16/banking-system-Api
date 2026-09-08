package com.excellence.bankingsystemapi.exceptions.account;


import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;

public class AccountNotActiveException extends BusinessException {

    public AccountNotActiveException() {

        super(
                "Account is not active.",
                ErrorCode.ACCOUNT_NOT_ACTIVE
        );
    }
}
