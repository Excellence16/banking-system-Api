

package com.excellence.bankingsystemapi.account.dto.response;

import com.excellence.bankingsystemapi.common.enums.Currency;
import lombok.*;

        import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountBalanceResponseDTO {

    private String accountNumber;

    private BigDecimal ledgerBalance;

    private BigDecimal availableBalance;

    private Currency currency;
}