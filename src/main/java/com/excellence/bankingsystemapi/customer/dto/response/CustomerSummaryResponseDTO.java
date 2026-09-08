package com.excellence.bankingsystemapi.customer.dto.response;

import com.excellence.bankingsystemapi.common.enums.CustomerStatus;
import lombok.Builder;

/**
 * Lightweight customer summary returned for
 * listings, search results and lookups where
 * full customer details are unnecessary.
 */
@Builder
public record CustomerSummaryResponseDTO(

        // Bank-generated unique customer number
        String customerNumber,

        // Customer name
        String firstName,

        String lastName,

        // Contact information
        String phoneNumber,

        String email,

        // Current customer status
        CustomerStatus status

) {

}

/*
 * Class Overview
 * --------------------------------------------------------------------
 * - Lightweight customer projection.
 * - Reduces payload size for search and list endpoints.
 * - Prevents exposing unnecessary customer information.
 * - Immutable implementation using Java Record.
 * - Suitable for dashboards, dropdowns, tables and reports.
 * - Designed for Spring Boot 3 / Java 21 enterprise applications.
 */