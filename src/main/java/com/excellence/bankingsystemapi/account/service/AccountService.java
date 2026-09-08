
package com.excellence.bankingsystemapi.account.service;

import com.excellence.bankingsystemapi.account.dto.request.AccountSearchRequestDTO;
import com.excellence.bankingsystemapi.account.dto.request.CreateAccountRequestDTO;
import com.excellence.bankingsystemapi.account.dto.request.UpdateAccountRequestDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountBalanceResponseDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountResponseDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountSummaryResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {

    /**
     * Opens a new account for an existing customer.
     */
    AccountResponseDTO createAccount(
            CreateAccountRequestDTO requestDTO
    );

    /**
     * Retrieves an account using its account number.
     */
    AccountResponseDTO getAccountByAccountNumber(
            String accountNumber
    );

    /**
     * Retrieves all accounts belonging to a customer.
     */
    List<AccountSummaryResponseDTO> getAccountsByCustomerNumber(
            String customerNumber
    );

    /**
     * Retrieves only account balance information.
     */
    AccountBalanceResponseDTO getAccountBalance(
            String accountNumber
    );

    /**
     * Updates editable account information.
     */
    AccountResponseDTO updateAccount(
            String accountNumber,
            UpdateAccountRequestDTO requestDTO
    );


    Page<AccountSummaryResponseDTO> searchAccounts(
            AccountSearchRequestDTO requestDTO);

    /**
     * Closes an account.
     */
    void closeAccount(
            String accountNumber
    );
    /**
     * Freezes an active account.
     */
    void freezeAccount(
            String accountNumber
    );

    /**
     * Removes the freeze from an account.
     * OR
     * UNFREEZE AN ACCOUNT
     */
    void unfreezeAccount(
            String accountNumber
    );

    /**
     * Marks an active account as dormant.
     */
    void markAccountDormant(
            String accountNumber
    );

    /**
     * Reactivates a dormant account.
     */
    void reactivateAccount(
            String accountNumber
    );

}
