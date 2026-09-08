

package com.excellence.bankingsystemapi.exceptions.account;


import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;

public class AccountNotFrozenException extends BusinessException {

    public AccountNotFrozenException() {

        super(
                "Account is not frozen.",
                ErrorCode.ACCOUNT_NOT_FROZEN
        );
    }
}