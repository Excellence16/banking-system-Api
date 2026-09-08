package com.excellence.bankingsystemapi.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCustomerRequestDTO {

    @NotBlank(message = "Residential address is required.")
    @Size(max = 300, message = "Residential address must not exceed 300 characters.")
    private String residentialAddress;

    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = "^(\\+234|0)[789][01]\\d{8}$",
            message = "Please provide a valid Nigerian phone number."
    )
    private String phoneNumber;

    @NotBlank(message = "Email address is required.")
    @Email(message = "Please provide a valid email address.")
    @Size(max = 150, message = "Email address must not exceed 150 characters.")
    private String email;

}

/*
 * Class Overview
 * ------------------------------------------------------------------
 * - Represents the request payload for updating customer information.
 * - Allows modification of mutable customer fields only.
 * - Uses Bean Validation to ensure valid input before reaching the service layer.
 * - Keeps immutable fields (BVN, Customer Number, Name, DOB, etc.) protected.
 * - Designed for Spring Boot 3 / Java 21 enterprise applications.
 */