package com.excellence.bankingsystemapi.exceptions.account;


import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;

public class AccountNotDormantException extends BusinessException {

    public AccountNotDormantException() {

        super(
                "Account is not dormant.",
                ErrorCode.ACCOUNT_NOT_DORMANT
        );
    }
}
