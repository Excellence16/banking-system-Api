package com.excellence.bankingsystemapi.transaction.dto.request;


import jakarta.validation.constraints.NotBlank;


public record ReverseTransactionRequestDTO(

        @NotBlank(message = "Reversal reason is required") String reason) {
}
