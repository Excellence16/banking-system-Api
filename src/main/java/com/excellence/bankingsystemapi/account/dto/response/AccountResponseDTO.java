

package com.excellence.bankingsystemapi.account.dto.response;

import com.excellence.bankingsystemapi.common.enums.AccountStatus;
import com.excellence.bankingsystemapi.common.enums.AccountType;
import com.excellence.bankingsystemapi.common.enums.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDTO {

    private String accountNumber;

    private String accountName;

    private String customerNumber;

    private String customerName;

    private AccountType accountType;

    private AccountStatus status;

    private Currency currency;

    private BigDecimal ledgerBalance;

    private BigDecimal availableBalance;

    private LocalDateTime createdAt;
}