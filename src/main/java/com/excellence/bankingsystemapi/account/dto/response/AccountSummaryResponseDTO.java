

package com.excellence.bankingsystemapi.account.dto.response;

import com.excellence.bankingsystemapi.common.enums.AccountStatus;
import com.excellence.bankingsystemapi.common.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountSummaryResponseDTO {

    private String accountNumber;

    private String accountName;

    private AccountType accountType;

    private AccountStatus status;

    private BigDecimal availableBalance;
}
