package com.excellence.bankingsystemapi.transaction.dto.request;

import com.excellence.bankingsystemapi.common.enums.TransactionChannel;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.DecimalMin;



import java.math.BigDecimal;




public record TransferRequestDTO (

@NotBlank(message = "Source account number is required ")
String sourceAccountNumber,

@NotBlank(message = "Destination account number is required")
String destinationAccountNumber,

@NotNull(message = "Transfer amount is required")
@DecimalMin(value = "0.01", message = "Transfer amount must be greater than zero")
 BigDecimal amount,

@NotNull(message = "Transaction channel is required")
TransactionChannel channel,

String description

){}
