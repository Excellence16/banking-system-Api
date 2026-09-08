package com.excellence.bankingsystemapi.exceptions.account;


import com.excellence.bankingsystemapi.exceptions.global.ErrorCode;

public class CustomerInactiveException extends BusinessException {

    public CustomerInactiveException() {

        super(
                "Only active customers can perform this operation.",
                ErrorCode.CUSTOMER_NOT_ACTIVE
        );
    }
}
