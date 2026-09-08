package com.excellence.bankingsystemapi.account.dto.request;


import com.excellence.bankingsystemapi.common.enums.AccountType;
import com.excellence.bankingsystemapi.common.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountRequestDTO {

    @NotBlank(message = "Customer number is required.")
    private String customerNumber;

    @NotNull(message = "Account type is required.")
    private AccountType accountType;

    @Builder.Default
    private Currency currency = Currency.NGN;
}