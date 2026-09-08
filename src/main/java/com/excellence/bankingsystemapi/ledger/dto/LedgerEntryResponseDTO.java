package com.excellence.bankingsystemapi.ledger.dto;

import com.excellence.bankingsystemapi.common.enums.EntryType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LedgerEntryResponseDTO {
    private EntryType entryType;

    private BigDecimal amount;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private LocalDateTime createdAt;


}
