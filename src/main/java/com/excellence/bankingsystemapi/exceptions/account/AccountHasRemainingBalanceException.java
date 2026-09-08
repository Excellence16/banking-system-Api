package com.excellence.bankingsystemapi.exceptions.account;

import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;






public class AccountHasRemainingBalanceException extends BusinessException {

    public AccountHasRemainingBalanceException () {

        super(
                "Account is already closed.",
                ErrorCode.ACCOUNT_ALREADY_CLOSED
        );
    }
}
