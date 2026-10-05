package com.excellence.bankingsystemapi.transaction.service;


import com.excellence.bankingsystemapi.transaction.dto.request.DepositRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.request.ReverseTransactionRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.request.TransferRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.request.WithdrawalRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.response.TransactionResponseDTO;


public interface TransactionService {


    TransactionResponseDTO deposit(DepositRequestDTO request);


    TransactionResponseDTO withdraw(WithdrawalRequestDTO request);


    TransactionResponseDTO transfer(TransferRequestDTO request);


    TransactionResponseDTO reverseTransaction(String transactionReference,
            ReverseTransactionRequestDTO request);

    TransactionResponseDTO getTransactionByReference(String transactionReference);
}




