package com.excellence.bankingsystemapi.customer.dto.response;

import com.excellence.bankingsystemapi.common.enums.CustomerStatus;
import com.excellence.bankingsystemapi.common.enums.Gender;
import com.excellence.bankingsystemapi.common.enums.KycLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO returned for customer-related requests.
 */
@Builder
public record CustomerResponseDTO(

        // Database identifier
        Long id,

        // Bank generated customer number
        String customerNumber,

        // Customer personal details
        String firstName,

        String middleName,

        String lastName,

        // Masked sensitive information
        String bvn,

        String nationalId,

        // Demographic information
        LocalDate dateOfBirth,

        Gender gender,

        String nationality,

        // Contact information
        String residentialAddress,

        String phoneNumber,

        String email,

        // Banking information
        CustomerStatus status,

        KycLevel kycLevel,

        // Audit information
        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {

}

/*
 * Class Overview
 * --------------------------------------------------------------------
 * - Represents customer information returned to API consumers.
 * - Exposes only safe customer data.
 * - Sensitive fields (BVN and National ID) should already be masked
 *   inside CustomerMapper before reaching this DTO.
 * - Immutable implementation using Java Record.
 * - Designed for Spring Boot 3 / Java 21 enterprise applications.
 */