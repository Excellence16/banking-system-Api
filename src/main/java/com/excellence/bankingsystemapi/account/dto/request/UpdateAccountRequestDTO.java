package com.excellence.bankingsystemapi.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountRequestDTO {

    @NotBlank(message = "Account name is required.")
    @Size(max = 100, message = "Account name must not exceed 100 characters.")
    private String accountName;
}