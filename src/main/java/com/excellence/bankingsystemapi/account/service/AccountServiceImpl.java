package com.excellence.bankingsystemapi.account.service;

import com.excellence.bankingsystemapi.account.dto.request.AccountSearchRequestDTO;
import com.excellence.bankingsystemapi.account.dto.request.CreateAccountRequestDTO;
import com.excellence.bankingsystemapi.account.dto.request.UpdateAccountRequestDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountBalanceResponseDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountResponseDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountSummaryResponseDTO;
import com.excellence.bankingsystemapi.account.mapper.AccountMapper;
import com.excellence.bankingsystemapi.account.repository.AccountRepository;
import com.excellence.bankingsystemapi.common.enums.AccountStatus;
import com.excellence.bankingsystemapi.common.enums.AccountType;
import com.excellence.bankingsystemapi.common.enums.CustomerStatus;
import com.excellence.bankingsystemapi.customer.repository.CustomerRepository;
import com.excellence.bankingsystemapi.exceptions.account.*;
import com.excellence.bankingsystemapi.exceptions.global.ResourceNotFoundException;
import com.excellence.bankingsystemapi.model.Account;
import com.excellence.bankingsystemapi.model.Customer;
import com.excellence.bankingsystemapi.specification.AccountSpecification;
import com.excellence.bankingsystemapi.util.AccountNumberGenerator;
import com.excellence.bankingsystemapi.util.UniqueCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final CustomerRepository customerRepository;

    private final AccountNumberGenerator accountNumberGenerator;


    //===========================================================
    // CREATE ACCOUNT
    //===========================================================

    @Override
    @Transactional
    public AccountResponseDTO createAccount(CreateAccountRequestDTO requestDTO) {


        log.info("Creating account for customer {}",
                requestDTO.getCustomerNumber());


        Customer customer = getCustomer(requestDTO.getCustomerNumber());


        validateCustomerIsActive(customer);


        if (accountAlreadyExists(customer, requestDTO.getAccountType())) {


            log.warn("Customer {} already owns account type {}",
                    customer.getCustomerNumber(),
                    requestDTO.getAccountType());


            throw new DuplicateAccountException();
        }


        String accountNumber = generateUniqueAccountNumber();


        Account account = buildAccount(accountNumber, customer, requestDTO);


        Account savedAccount = accountRepository.save(account);


        log.info("Account {} created successfully",
                savedAccount.getAccountNumber());


        return accountMapper.toResponse(savedAccount);
    }


    //===========================================================
    // RETRIEVE ACCOUNT
    //===========================================================

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDTO getAccountByAccountNumber(String accountNumber) {


        Account account = getAccount(accountNumber);


        return accountMapper.toResponse(account);
    }


    @Override
    @Transactional(readOnly = true)
    public List<AccountSummaryResponseDTO> getAccountsByCustomerNumber(String customerNumber) {


        Customer customer = getCustomer(customerNumber);


        List<Account> accounts = accountRepository.findByCustomerAndDeletedFalse(
                customer);


        return accounts.stream().map(accountMapper::toSummaryResponse).toList();
    }


    @Override
    @Transactional(readOnly = true)
    public AccountBalanceResponseDTO getAccountBalance(String accountNumber) {


        Account account = getAccount(accountNumber);


        return accountMapper.toBalanceResponse(account);
    }


    //===========================================================
    // UPDATE ACCOUNT
    //===========================================================


    @Override
    @Transactional
    public AccountResponseDTO updateAccount(String accountNumber,
            UpdateAccountRequestDTO requestDTO) {


        Account account = getAccount(accountNumber);


        validateAccountIsNotClosed(account);


        account.setAccountName(requestDTO.getAccountName());


        Account updatedAccount = accountRepository.save(account);


        log.info("Account {} updated successfully", accountNumber);


        return accountMapper.toResponse(updatedAccount);
    }


    //===========================================================
    // CLOSE ACCOUNT
    //===========================================================


    @Override
    @Transactional
    public void closeAccount(String accountNumber) {


        Account account = getAccount(accountNumber);


        validateAccountIsNotClosed(account);


        validateZeroLedgerBalance(account);


        close(account);


        accountRepository.save(account);


        log.info("Account {} closed successfully", accountNumber);
    }


    //===========================================================
    // ACCOUNT STATUS MANAGEMENT
    //===========================================================


    @Override
    @Transactional
    public void freezeAccount(String accountNumber) {


        Account account = getAccount(accountNumber);


        validateAccountIsActive(account);


        account.setStatus(AccountStatus.FROZEN);


        accountRepository.save(account);


        log.info("Account {} frozen", accountNumber);
    }


    @Override
    @Transactional
    public void unfreezeAccount(String accountNumber) {


        Account account = getAccount(accountNumber);


        validateAccountIsFrozen(account);


        account.setStatus(AccountStatus.ACTIVE);


        accountRepository.save(account);


        log.info("Account {} unfrozen", accountNumber);
    }

    @Override
    @Transactional
    public void markAccountDormant(String accountNumber) {


        Account account = getAccount(accountNumber);


        validateAccountIsActive(account);


        account.setStatus(AccountStatus.DORMANT);


        accountRepository.save(account);


        log.info("Account {} marked as dormant", accountNumber);
    }


    @Override
    @Transactional
    public void reactivateAccount(String accountNumber) {


        Account account = getAccount(accountNumber);


        validateAccountIsDormant(account);


        account.setStatus(AccountStatus.ACTIVE);


        accountRepository.save(account);


        log.info("Account {} reactivated successfully", accountNumber);
    }


    //===========================================================
    // ACCOUNT SEARCH
    //===========================================================


    @Override
    @Transactional(readOnly = true)
    public Page<AccountSummaryResponseDTO> searchAccounts(
            AccountSearchRequestDTO requestDTO) {


        Sort sort = Sort.by(Sort.Direction.fromString(requestDTO
                .getSortDirection()
                .name()), requestDTO.getSortBy());


        Pageable pageable = PageRequest.of(requestDTO.getPage(),
                requestDTO.getSize(),
                sort);


        Specification<Account> specification = AccountSpecification.build(
                requestDTO);


        return accountRepository
                .findAll(specification, pageable)
                .map(accountMapper::toSummaryResponse);
    }


    //===========================================================
    // PRIVATE HELPER METHODS
    //===========================================================


    /**
     * Retrieves customer by customer number.
     */
    private Customer getCustomer(String customerNumber) {


        return customerRepository
                .findByCustomerNumberAndDeletedFalse(customerNumber)
                .orElseThrow(() -> {


                    log.warn("Customer {} not found", customerNumber);


                    return new ResourceNotFoundException(
                            "Customer not found with customer number: " + customerNumber);
                });
    }


    /**
     * Ensures customer is allowed to create an account.
     */
    private void validateCustomerIsActive(Customer customer) {


        if (customer.getStatus() != CustomerStatus.ACTIVE) {


            log.warn("Inactive customer {} attempted account creation",
                    customer.getCustomerNumber());


            throw new CustomerInactiveException();
        }
    }


    /**
     * Checks duplicate account type.
     */
    private boolean accountAlreadyExists(Customer customer,
            AccountType accountType) {


        return accountRepository.existsByCustomerAndAccountType(customer,
                accountType);
    }


    /**
     * Generates unique account number.
     */
    private String generateUniqueAccountNumber() {


        return UniqueCodeGenerator.generateUnique(accountNumberGenerator::generate,
                accountRepository::existsByAccountNumber);
    }


    /**
     * Builds Account entity.
     */
    private Account buildAccount(String accountNumber,
            Customer customer,
            CreateAccountRequestDTO requestDTO) {


        return Account
                .builder()
                .accountNumber(accountNumber)
                .customer(customer)
                .accountName(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName())
                .accountType(requestDTO.getAccountType())
                .currency(requestDTO.getCurrency())
                .status(AccountStatus.ACTIVE)
                .ledgerBalance(BigDecimal.ZERO)
                .availableBalance(BigDecimal.ZERO)
                .build();
    }


    /**
     * Retrieves account.
     */
    private Account getAccount(String accountNumber) {


        return accountRepository
                .findByAccountNumberAndDeletedFalse(accountNumber)
                .orElseThrow(() -> {


                    log.warn("Account {} not found", accountNumber);


                    return new ResourceNotFoundException(
                            "Account not found with account number: " + accountNumber);
                });
    }


    /**
     * Validates account is not already closed.
     */
    private void validateAccountIsNotClosed(Account account) {


        if (account.getStatus() == AccountStatus.CLOSED) {


            log.warn("Operation attempted on closed account {}",
                    account.getAccountNumber());


            // line 592, inside validateAccountIsNotClosed
            throw new AccountAlreadyClosedException("Account " + account.getAccountNumber() + " is already closed.");
        }
    }


    /**
     * Ensures account has zero ledger balance before closing.
     */
    private void validateZeroLedgerBalance(Account account) {


        if (account.getLedgerBalance().compareTo(BigDecimal.ZERO) != 0) {


            log.warn("Account {} cannot close because balance is {}",
                    account.getAccountNumber(),
                    account.getLedgerBalance());


            // line 617, inside validateZeroLedgerBalance
            throw new InsufficientBalanceException("Account " + account.getAccountNumber() + " cannot be closed because balance is " + account.getLedgerBalance());
        }
    }


    /**
     * Performs account closure.
     */
    private void close(Account account) {


        account.setStatus(AccountStatus.CLOSED);

        account.setClosedAt(LocalDateTime.now());
    }


    /**
     * Ensures account is active.
     */
    private void validateAccountIsActive(Account account) {


        if (account.getStatus() != AccountStatus.ACTIVE) {


            log.warn("Account {} is not active", account.getAccountNumber());


            throw new AccountNotActiveException();
        }
    }


    /**
     * Ensures account is frozen.
     */
    private void validateAccountIsFrozen(Account account) {


        if (account.getStatus() != AccountStatus.FROZEN) {


            log.warn("Account {} is not frozen", account.getAccountNumber());


            throw new AccountNotFrozenException();
        }
    }


    /**
     * Ensures account is dormant.
     */
    private void validateAccountIsDormant(Account account) {


        if (account.getStatus() != AccountStatus.DORMANT) {


            log.warn("Account {} is not dormant", account.getAccountNumber());


            throw new AccountNotDormantException();
        }
    }

}