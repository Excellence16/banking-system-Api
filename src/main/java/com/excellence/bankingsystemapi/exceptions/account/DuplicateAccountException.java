package com.excellence.bankingsystemapi.exceptions.account;


import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;

public class DuplicateAccountException extends BusinessException {

    public DuplicateAccountException() {

        super(
                "Customer already owns this type of account.",
                ErrorCode.DUPLICATE_ACCOUNT
        );
    }
}
