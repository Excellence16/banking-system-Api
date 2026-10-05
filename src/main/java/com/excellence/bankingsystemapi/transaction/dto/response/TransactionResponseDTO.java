package com.excellence.bankingsystemapi.transaction.dto.response;

import com.excellence.bankingsystemapi.common.enums.TransactionChannel;
import com.excellence.bankingsystemapi.common.enums.TransactionStatus;
import com.excellence.bankingsystemapi.common.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDTO(


        UUID transactionId,

        String transactionReference,

        TransactionType transactionType,

        TransactionStatus transactionStatus,

        BigDecimal amount,

        TransactionChannel channel,

        //SENDER SNAPSHOT
        String senderName,

        String senderAccountNumber,

        String senderBank,

        //RECEIVER SNAPSHOT
        String receiverName,

        String receiverBank,

        String receiverAccountNumber,

        String description,

        LocalDateTime createdAt) {
}
