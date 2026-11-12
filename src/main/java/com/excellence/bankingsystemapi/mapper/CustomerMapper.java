package com.excellence.bankingsystemapi.mapper;

import com.excellence.bankingsystemapi.common.enums.CustomerStatus;
import com.excellence.bankingsystemapi.common.enums.KycLevel;
import com.excellence.bankingsystemapi.customer.dto.request.CreateCustomerRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.request.UpdateCustomerRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.response.CustomerResponseDTO;
import com.excellence.bankingsystemapi.customer.dto.response.CustomerSummaryResponseDTO;
import com.excellence.bankingsystemapi.model.Customer;

public final class CustomerMapper {

    // Prevent instantiation
    private CustomerMapper() {
    }

    /**
     * Converts CreateCustomerRequestDTO to Customer entity.
     */
    public static Customer toEntity(CreateCustomerRequestDTO request) {

        return Customer
                .builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .bvn(request.getBvn())
                .nationalId(request.getNationalId())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .nationality(request.getNationality())
                .residentialAddress(request.getResidentialAddress())
                .phoneNumber(request.getPhoneNumber())

                // NO LONGER IN USE !!!
                // .email(request.getEmail())

                // Default values for newly created customers
                .status(CustomerStatus.ACTIVE)
                .kycLevel(KycLevel.LEVEL_1)

                .build();
    }

    /**
     * Converts Customer entity to CustomerResponseDTO.
     */
    public static CustomerResponseDTO toResponse(Customer customer) {

        if (customer == null) {
            return null;
        }

        return CustomerResponseDTO
                .builder()
                .id(customer.getId())
                .customerNumber(customer.getCustomerNumber())
                .firstName(customer.getFirstName())
                .middleName(customer.getMiddleName())
                .lastName(customer.getLastName())
                .bvn(maskSensitive(customer.getBvn()))
                .nationalId(maskSensitive(customer.getNationalId()))
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .nationality(customer.getNationality())
                .residentialAddress(customer.getResidentialAddress())
                .phoneNumber(customer.getPhoneNumber())

                // NO LONGER IN USE !!!
                //.email(customer.getEmail())

                .status(customer.getStatus())
                .kycLevel(customer.getKycLevel())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    /**
     * Converts Customer entity to lightweight summary DTO.
     */
    public static CustomerSummaryResponseDTO toSummary(Customer customer) {

        if (customer == null) {
            return null;
        }

        return CustomerSummaryResponseDTO
                .builder()
                .customerNumber(customer.getCustomerNumber())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(customer.getPhoneNumber())

                // NO LONGER IN USE !!!
                //  .email(customer.getEmail())
                .status(customer.getStatus())
                .build();

    }

    /**
     * Updates mutable customer fields only.
     */
    public static void updateCustomerFromDto(UpdateCustomerRequestDTO request,
            Customer customer) {

        customer.setResidentialAddress(request.getResidentialAddress());
        customer.setPhoneNumber(request.getPhoneNumber());

        // NO LONGER IN USE !!!

        // customer.setEmail(request.getEmail());
    }

    /**
     * Masks sensitive values like BVN and National ID.
     */
    private static String maskSensitive(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        if (value.length() <= 4) {
            return "*".repeat(value.length());
        }

        return "*".repeat(value.length() - 4) + value.substring(value.length() - 4);
    }

    /**
     * Builds customer's full name.
     */
    private static String buildFullName(Customer customer) {

        StringBuilder fullName = new StringBuilder();

        fullName.append(customer.getFirstName());

        if (customer.getMiddleName() != null && !customer
                .getMiddleName()
                .isBlank()) {

            fullName.append(" ").append(customer.getMiddleName());
        }

        fullName.append(" ").append(customer.getLastName());

        return fullName.toString().trim();
    }

}

/*
 * Class Overview
 * -------------------------------------------------------------------------
 * - Centralizes all Customer ↔ DTO conversions.
 * - Creates Customer entities from request DTOs.
 * - Converts Customer entities to response DTOs.
 * - Provides lightweight summary mapping.
 * - Updates only mutable customer fields.
 * - Masks sensitive information before sending responses.
 * - Prevents duplicate mapping logic across the application.
 * - Designed for Spring Boot 3 / Java 21 enterprise applications.
 */