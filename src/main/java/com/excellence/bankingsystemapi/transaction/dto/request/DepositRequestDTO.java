package com.excellence.bankingsystemapi.transaction.dto.request;

import com.excellence.bankingsystemapi.common.enums.TransactionChannel;
import jakarta.validation.constraints.NotBlank;


import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;



public record DepositRequestDTO (

    @NotBlank(message = "Account number is required")
    String accountNumber,

    @NotNull(message = "Deposit amount is required")
    @DecimalMin(value = "0.01", message = "Deposit amount must be greater than zero")
    BigDecimal amount,

    @NotNull(message = "Transaction channel is required")
    TransactionChannel channel,



    String description


    ){}
