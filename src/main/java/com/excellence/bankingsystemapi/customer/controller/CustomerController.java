package com.excellence.bankingsystemapi.customer.controller;

import com.excellence.bankingsystemapi.customer.dto.request.CreateCustomerRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.request.CustomerSearchRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.request.UpdateCustomerRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.response.ApiResponse;
import com.excellence.bankingsystemapi.customer.dto.response.ApiResponseFactory;
import com.excellence.bankingsystemapi.customer.dto.response.CustomerResponseDTO;
import com.excellence.bankingsystemapi.customer.dto.response.PageResponse;
import com.excellence.bankingsystemapi.customer.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller responsible for customer management operations.
 *
 * Handles:
 * - Customer creation
 * - Customer retrieval
 * - Customer search
 * - Customer updates
 * - Customer deactivation
 *
 * All responses follow the standard ApiResponse wrapper.
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {


    private final CustomerService customerService;



    /**
     * Creates a new customer.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(
            @Valid @RequestBody CreateCustomerRequestDTO request,
            HttpServletRequest httpRequest
    ) {


        CustomerResponseDTO customer =
                customerService.createCustomer(request);


        ApiResponse<CustomerResponseDTO> response =
                ApiResponseFactory.success(
                        "Customer created successfully.",
                        customer,
                        HttpStatus.CREATED,
                        httpRequest
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }



    /**
     * Retrieves customer by database ID.
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerById(
            @PathVariable Long customerId,
            HttpServletRequest httpRequest
    ) {


        CustomerResponseDTO customer =
                customerService.getCustomerById(customerId);


        ApiResponse<CustomerResponseDTO> response =
                ApiResponseFactory.success(
                        "Customer retrieved successfully.",
                        customer,
                        HttpStatus.OK,
                        httpRequest
                );


        return ResponseEntity.ok(response);
    }



    /**
     * Retrieves customer by customer number.
     */
    @GetMapping("/customer-number/{customerNumber}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerByCustomerNumber(
            @PathVariable String customerNumber,
            HttpServletRequest httpRequest
    ) {


        CustomerResponseDTO customer =
                customerService.getCustomerByCustomerNumber(customerNumber);


        ApiResponse<CustomerResponseDTO> response =
                ApiResponseFactory.success(
                        "Customer retrieved successfully.",
                        customer,
                        HttpStatus.OK,
                        httpRequest
                );


        return ResponseEntity.ok(response);
    }



    /**
     * Searches customers with:
     *
     * - Pagination
     * - Sorting
     * - Filtering
     * - Keyword search
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponseDTO>>> searchCustomers(
            @Valid CustomerSearchRequestDTO request,
            HttpServletRequest httpRequest
    ) {


        PageResponse<CustomerResponseDTO> customers =
                customerService.searchCustomers(request);



        ApiResponse<PageResponse<CustomerResponseDTO>> response =
                ApiResponseFactory.success(
                        "Customers retrieved successfully.",
                        customers,
                        HttpStatus.OK,
                        httpRequest
                );


        return ResponseEntity.ok(response);
    }



    /**
     * Updates customer information.
     */
    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerRequestDTO request,
            HttpServletRequest httpRequest
    ) {


        CustomerResponseDTO customer =
                customerService.updateCustomer(
                        customerId,
                        request
                );


        ApiResponse<CustomerResponseDTO> response =
                ApiResponseFactory.success(
                        "Customer updated successfully.",
                        customer,
                        HttpStatus.OK,
                        httpRequest
                );


        return ResponseEntity.ok(response);
    }



    /**
     * Deactivates customer using soft delete.
     *
     * Customer data remains available for:
     * - Audit
     * - Compliance
     * - Reporting
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Void>> deactivateCustomer(
            @PathVariable Long customerId,
            HttpServletRequest httpRequest
    ) {


        customerService.deactivateCustomer(customerId);



        ApiResponse<Void> response =
                ApiResponseFactory.success(
                        "Customer deactivated successfully.",
                        null,
                        HttpStatus.OK,
                        httpRequest
                );


        return ResponseEntity.ok(response);
    }

}