package com.excellence.bankingsystemapi.customer.service;

import com.excellence.bankingsystemapi.common.PageResponse;
import com.excellence.bankingsystemapi.common.enums.CustomerStatus;
import com.excellence.bankingsystemapi.common.pagination.SortFieldValidator;
import com.excellence.bankingsystemapi.constants.CustomerSearchConstants;
import com.excellence.bankingsystemapi.customer.dto.request.CreateCustomerRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.request.CustomerSearchRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.request.UpdateCustomerRequestDTO;
import com.excellence.bankingsystemapi.customer.dto.response.CustomerResponseDTO;
import com.excellence.bankingsystemapi.customer.repository.CustomerRepository;
import com.excellence.bankingsystemapi.exceptions.global.DuplicateResourceException;
import com.excellence.bankingsystemapi.exceptions.global.ResourceNotFoundException;
import com.excellence.bankingsystemapi.mapper.CustomerMapper;
import com.excellence.bankingsystemapi.mapper.PageResponseMapper;
import com.excellence.bankingsystemapi.model.Customer;
import com.excellence.bankingsystemapi.specification.CustomerSpecification;
import com.excellence.bankingsystemapi.util.CustomerNumberGenerator;
import com.excellence.bankingsystemapi.util.UniqueCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepository customerRepository;


    /**
     * Creates a new customer.
     */
    @Override
    public CustomerResponseDTO createCustomer(CreateCustomerRequestDTO request) {

        log.info("Creating new customer with email: {}", request.getEmail());

        validateCreateCustomer(request);
        System.out.println("Checking National ID: " + request.getNationalId());


        Customer customer = CustomerMapper.toEntity(request);

        // Generates unique customer number before persistence
        String customerNumber = UniqueCodeGenerator.generateUnique(
                CustomerNumberGenerator::generate,
                customerRepository::existsByCustomerNumber);

        customer.setCustomerNumber(customerNumber);

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created successfully with customer number: {}",
                savedCustomer.getCustomerNumber());

        return CustomerMapper.toResponse(savedCustomer);
    }


    /**
     * Updates customer mutable information.
     */
    @Override
    public CustomerResponseDTO updateCustomer(Long customerId,
            UpdateCustomerRequestDTO request) {

        log.info("Updating customer with id: {}", customerId);

        Customer customer = findCustomerById(customerId);

        validateUpdateCustomer(customer, request);

        CustomerMapper.updateCustomerFromDto(request, customer);

        Customer updatedCustomer = customerRepository.save(customer);

        log.info("Customer updated successfully with id: {}", customerId);

        return CustomerMapper.toResponse(updatedCustomer);
    }


    /**
     * Retrieves customer by database ID.
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Long customerId) {

        return CustomerMapper.toResponse(findCustomerById(customerId));
    }


    /**
     * Retrieves customer by customer number.
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerByCustomerNumber(String customerNumber) {

        Customer customer = customerRepository
                .findByCustomerNumberAndDeletedFalse(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with customer number: " + customerNumber));


        return CustomerMapper.toResponse(customer);
    }


    /**
     * Searches customers using:
     * <p>
     * - Keyword search
     * - Filtering
     * - Sorting
     * - Pagination
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerResponseDTO> searchCustomers(
            CustomerSearchRequestDTO request) {

        Pageable pageable = buildPageable(request);

        SortFieldValidator.validate(pageable,
                CustomerSearchConstants.ALLOWED_SORT_FIELDS);


        Page<Customer> customers = customerRepository.findAllByDeletedFalse(
                CustomerSpecification.search(request),
                pageable);


        Page<CustomerResponseDTO> responsePage = customers.map(CustomerMapper::toResponse);


        return PageResponseMapper.from(responsePage);
    }


    /**
     * Performs soft delete.
     * <p>
     * Customer records are never physically removed.
     */
    @Override
    public void deactivateCustomer(Long customerId) {

        Customer customer = findCustomerById(customerId);


        customer.setDeleted(true);

        customer.setStatus(CustomerStatus.INACTIVE);


        customerRepository.save(customer);


        log.info("Customer deactivated successfully with id: {}", customerId);
    }



    /*
     ============================================================
     Helper Methods
     ============================================================
     */


    /**
     * Finds active customer by ID.
     */
    private Customer findCustomerById(Long customerId) {

        return customerRepository
                .findByIdAndDeletedFalse(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + customerId));
    }


    /**
     * Validates customer creation rules.
     */
    private void validateCreateCustomer(CreateCustomerRequestDTO request) {


        if (customerRepository.existsByBvn(request.getBvn())) {

            throw new DuplicateResourceException("BVN already exists.");
        }

        if (customerRepository.existsByNationalId(request.getNationalId())) {

            throw new DuplicateResourceException("National ID already exists.");
        }


        if (customerRepository.existsByEmailAndDeletedFalse(request.getEmail())) {

            throw new DuplicateResourceException("Email already exists.");
        }


        if (customerRepository.existsByPhoneNumberAndDeletedFalse(request.getPhoneNumber())) {

            throw new DuplicateResourceException("Phone number already exists.");
        }
        System.out.println("Checking National ID: " + request.getNationalId());

        boolean nationalIdExists = customerRepository.existsByNationalId(request.getNationalId());

        System.out.println("National ID exists: " + nationalIdExists);

        if (nationalIdExists) {

            throw new DuplicateResourceException("National ID already exists.");
        }

    }


    /**
     * Validates customer update rules.
     */
    private void validateUpdateCustomer(Customer customer,
            UpdateCustomerRequestDTO request) {


        if (!customer
                .getEmail()
                .equalsIgnoreCase(request.getEmail()) && customerRepository.existsByEmail(
                request.getEmail(),
                customer.getId())) {

            throw new DuplicateResourceException("Email already exists.");
        }


        if (!customer
                .getPhoneNumber()
                .equals(request.getPhoneNumber()) && customerRepository.existsByPhoneNumber(
                request.getPhoneNumber(),
                customer.getId())) {

            throw new DuplicateResourceException("Phone number already exists.");
        }


    }


    /**
     * Builds pageable object from search request.
     */
    private Pageable buildPageable(CustomerSearchRequestDTO request) {


        int page = request.getPage() == null ? CustomerSearchConstants.DEFAULT_PAGE : request.getPage();


        int size = request.getSize() == null ? CustomerSearchConstants.DEFAULT_SIZE : request.getSize();


        String sortBy = request.getSortBy() == null ? CustomerSearchConstants.DEFAULT_SORT_BY : request.getSortBy();


        Sort.Direction direction = request.getSortDirection() == null ? Sort.Direction.DESC : Sort.Direction.valueOf(
                request.getSortDirection().name());


        return PageRequest.of(page, size, Sort.by(direction, sortBy));

    }


}

/*
 * Class Overview
 * --------------------------------------------------------------------
 * - Core business layer for Customer operations.
 * - Handles creation, update, retrieval, searching and deactivation.
 * - Implements pagination, filtering, sorting and specification queries.
 * - Uses soft delete instead of physical deletion.
 * - Centralizes validation and reusable business logic.
 * - Uses transactional boundaries for data consistency.
 * - Designed for Spring Boot 3 / Java 21 enterprise banking systems.
 */