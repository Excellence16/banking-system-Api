package com.excellence.bankingsystemapi.transaction.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TransactionRequestDTO {

    @NotBlank(message = "Destination ACcountNumber is Required ")
    private  String destinationAccountNumber;

    @DecimalMin(value = "1.00", message = "Amount must be Greater than zero")
    private BigDecimal amount;

    private String description;

}
