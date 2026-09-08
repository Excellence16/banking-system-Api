package com.excellence.bankingsystemapi.customer.service;

import com.excellence.bankingsystemapi.customer.dto.request.CreateCustomerRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.request.CustomerSearchRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.request.UpdateCustomerRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.response.CustomerResponseDTO;
import com.excellence.bankingsystemapi.customer.dto.response.PageResponse;

public interface CustomerService {

    /**
     * Creates a new customer.
     */
    CustomerResponseDTO createCustomer(
            CreateCustomerRequestDTO request
    );

    /**
     * Updates mutable customer information.
     */
    CustomerResponseDTO updateCustomer(
            Long customerId,
            UpdateCustomerRequestDTO request
    );

    /**
     * Retrieves a customer by database ID.
     */
    CustomerResponseDTO getCustomerById(
            Long customerId
    );

    /**
     * Retrieves a customer by customer number.
     */
    CustomerResponseDTO getCustomerByCustomerNumber(
            String customerNumber
    );

    /**
     * Searches customers using filtering,
     * sorting and pagination.
     */
    PageResponse<CustomerResponseDTO> searchCustomers(
            CustomerSearchRequestDTO request
    );

    /**
     * Soft deletes (deactivates) a customer.
     */
    void deactivateCustomer(
            Long customerId
    );

}

/*
 * Interface Overview
 * --------------------------------------------------------------------
 * - Defines customer business operations.
 * - Supports customer creation and updates.
 * - Supports customer retrieval.
 * - Supports pagination, filtering, sorting and search.
 * - Supports soft deletion (deactivation).
 * - Acts as the contract between the controller and service layer.
 * - Designed for Spring Boot 3 / Java 21 enterprise applications.
 */