package com.excellence.bankingsystemapi.account.dto.request;



import com.excellence.bankingsystemapi.common.enums.AccountStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeAccountStatusRequestDTO {

    @NotNull(message = "Account status is required.")
    private AccountStatus status;

    @NotBlank(message = "Reason is required.")
    @Size(max = 255, message = "Reason must not exceed 255 characters.")
    private String reason;
}