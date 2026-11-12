package com.excellence.bankingsystemapi.customer.dto.request;

import com.excellence.bankingsystemapi.common.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequestDTO {

    @NotBlank(message = "First name is required.")
    @Size(max = 100, message = "First name must not exceed 100 characters.")
    private String firstName;

    @Size(max = 100, message = "Middle name must not exceed 100 characters.")
    private String middleName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 100, message = "Last name must not exceed 100 characters.")
    private String lastName;

    // Nigerian Bank Verification Number (BVN)
    @NotBlank(message = "BVN is required.")
    @Pattern(regexp = "^\\d{11}$", message = "BVN must contain exactly 11 digits.")
    private String bvn;

    @Size(max = 30, message = "National ID must not exceed 30 characters.")
    private String nationalId;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be in the past.")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required.")
    private Gender gender;

    @NotBlank(message = "Nationality is required.")
    @Size(max = 100, message = "Nationality must not exceed 100 characters.")
    private String nationality;

    @NotBlank(message = "Residential address is required.")
    @Size(max = 300, message = "Residential address must not exceed 300 characters.")
    private String residentialAddress;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^(\\+234|0)[789][01]\\d{8}$", message = "Please provide a valid Nigerian phone number.")
    private String phoneNumber;


    //NO LONGER IN USE

    // @NotBlank(message = "Email address is required.")
    // @Email(message = "Please provide a valid email address.")
    // @Size(max = 150, message = "Email address must not exceed 150
    // characters.")
    // private String email;

}

/*
 * Class Overview
 * ------------------------------------------------------------------
 * - Represents the request payload for creating a new customer.
 * - Uses Bean Validation (Jakarta Validation) for input validation.
 * - Matches the Customer entity field constraints.
 * - Prevents invalid data from reaching the service layer.
 * - Designed for Spring Boot 3 / Java 21 enterprise applications.
 */