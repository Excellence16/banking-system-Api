package com.excellence.bankingsystemapi.account.repository;



import com.excellence.bankingsystemapi.model.Account;
import com.excellence.bankingsystemapi.common.enums.AccountStatus;
import com.excellence.bankingsystemapi.common.enums.AccountType;
import com.excellence.bankingsystemapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>,
        JpaSpecificationExecutor<Account> {

    /**
     * Find an account by its unique account number.
     */
    Optional<Account> findByAccountNumberAndDeletedFalse(String accountNumber);

    /**
     * Check if an account number already exists.
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Find all accounts belonging to a customer.
     */
    List<Account> findByCustomerAndDeletedFalse(Customer customer);

    /**
     * Find all accounts belonging to a customer by customer number.
     */
    List<Account> findByCustomerCustomerNumber(String customerNumber);

    /**
     * Find all accounts with a particular status.
     */
    List<Account> findByStatus(AccountStatus status);

    /**
     * Find all accounts of a particular type.
     */
    List<Account> findByAccountType(AccountType accountType);

    /**
     * Find an account by account number and status.
     */
    Optional<Account> findByAccountNumberAndStatus(
            String accountNumber,
            AccountStatus status
    );

    /**
     * Count the number of accounts owned by a customer.
     */
    long countByCustomer(Customer customer);

    /**
     * Check whether a customer already owns a particular account type.
     */
    boolean existsByCustomerAndAccountType(
            Customer customer,
            AccountType accountType
    );
}