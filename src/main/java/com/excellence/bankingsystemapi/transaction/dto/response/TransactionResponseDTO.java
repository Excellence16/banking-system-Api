package com.excellence.bankingsystemapi.transaction.dto.response;

import com.excellence.bankingsystemapi.common.enums.TransactionType;

import com.excellence.bankingsystemapi.common.enums.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TransactionResponseDTO {

    private String reference;

    private TransactionType transactionType;

    private TransactionStatus transactionStatus;

    private BigDecimal amount;

    private String description;

    private  LocalDateTime createdAt;

}
