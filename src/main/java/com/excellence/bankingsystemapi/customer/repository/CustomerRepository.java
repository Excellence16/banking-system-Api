package com.excellence.bankingsystemapi.customer.repository;

import com.excellence.bankingsystemapi.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerRepository extends
        JpaRepository<Customer, Long>,
        JpaSpecificationExecutor<Customer> {

    // =========================
    // Customer Retrieval
    // =========================

    Optional<Customer> findByIdAndDeletedFalse(Long id);

    Optional<Customer> findByCustomerNumberAndDeletedFalse(String customerNumber);

    Page<Customer> findAllByDeletedFalse(Specification<Customer> spec,
            Pageable pageable);

    Optional<Customer> findByEmailAndDeletedFalse(String email);

    Optional<Customer> findByPhoneNumberAndDeletedFalse(String phoneNumber);

    Optional<Customer> findByBvnAndDeletedFalse(String bvn);

    // =========================
    // Duplicate Validation
    // =========================

    boolean existsByCustomerNumber(String customerNumber);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByPhoneNumberAndDeletedFalse(String phoneNumber);

    boolean existsByBvn(String bvn);

    boolean existsByNationalId(String NationalId);

    // =========================
    // Update Validation
    // Ignore the current customer
    // =========================

    boolean existsByEmail(
            String email,
            Long id
    );

    boolean existsByPhoneNumber(
            String phoneNumber,
            Long id
    );

    boolean existsByBvnAndIdNotAndDeletedFalse(
            String bvn,
            Long id
    );

    boolean existsByNationalIdAndIdNotAndDeletedFalse(String nationalId, Long id);

}