package com.excellence.bankingsystemapi.account.controller;


import com.excellence.bankingsystemapi.account.dto.request.AccountSearchRequestDTO;
import com.excellence.bankingsystemapi.account.dto.request.CreateAccountRequestDTO;
import com.excellence.bankingsystemapi.account.dto.request.UpdateAccountRequestDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountBalanceResponseDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountResponseDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountSummaryResponseDTO;
import com.excellence.bankingsystemapi.account.service.AccountService;
import com.excellence.bankingsystemapi.common.ApiResponse;
import com.excellence.bankingsystemapi.common.ApiResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;


    //===========================================================
    // CREATE ACCOUNT
    //===========================================================

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponseDTO>> createAccount(@Valid @RequestBody CreateAccountRequestDTO requestDTO,
            HttpServletRequest request) {


        AccountResponseDTO response = accountService.createAccount(requestDTO);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.success("Account created Successfully",
                        response,
                        HttpStatus.CREATED,
                        request));
    }
  

    //===========================================================
    // GET ACCOUNT BY ACCOUNT NUMBER
    //===========================================================

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> getAccountByNumber(@PathVariable String accountNumber,
            HttpServletRequest request) {


        AccountResponseDTO response = accountService.getAccountByAccountNumber(
                accountNumber);


        return ResponseEntity.ok(ApiResponseFactory.success(

                "Account retrieved successfully",
                response,
                HttpStatus.OK,
                request

        ));
    }


    //===========================================================
    // GET ALL CUSTOMER ACCOUNTS
    //===========================================================

    @GetMapping("/customer/{customerNumber}")
    public ResponseEntity<ApiResponse<List<AccountSummaryResponseDTO>>> getAccountsByCustomerNumber(
            @PathVariable String customerNumber,
            HttpServletRequest request) {


        List<AccountSummaryResponseDTO> response = accountService.getAccountsByCustomerNumber(
                customerNumber);


        return ResponseEntity.ok(ApiResponseFactory.success(
                "Accounts retrieved successfully",
                response,
                HttpStatus.OK,
                request

        ));
    }


    //===========================================================
    // GET ACCOUNT BALANCE
    //===========================================================

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<ApiResponse<AccountBalanceResponseDTO>> getAccountBalance(
            @PathVariable String accountNumber,
            HttpServletRequest request) {


        AccountBalanceResponseDTO response = accountService.getAccountBalance(
                accountNumber);


        return ResponseEntity.ok(ApiResponseFactory.success(
                "Account Balance retrieved successfully",
                response,
                HttpStatus.OK,
                request

        ));
    }


    //===========================================================
    // UPDATE ACCOUNT
    //===========================================================

    @PutMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> updateAccount(@PathVariable String accountNumber,

            @Valid @RequestBody UpdateAccountRequestDTO requestDTO,
            HttpServletRequest request) {


        AccountResponseDTO response = accountService.updateAccount(accountNumber,
                requestDTO);


        return ResponseEntity.ok(ApiResponseFactory.success(
                "Account updated successfully",
                response,
                HttpStatus.OK,
                request

        ));
    }


    //===========================================================
    // SEARCH ACCOUNTS
    //===========================================================

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AccountSummaryResponseDTO>>> searchAccounts(
            @Valid AccountSearchRequestDTO requestDTO,
            HttpServletRequest request) {


        Page<AccountSummaryResponseDTO> response = accountService.searchAccounts(
                requestDTO);


        return ResponseEntity.ok(ApiResponseFactory.success(
                "Accounts retrieved successfully",
                response,
                HttpStatus.OK,
                request

        ));
    }


    //===========================================================
    // FREEZE ACCOUNT
    //===========================================================

    @PatchMapping("/{accountNumber}/freeze")
    public ResponseEntity<ApiResponse<Void>> freezeAccount(@PathVariable String accountNumber,
            HttpServletRequest request) {


        accountService.freezeAccount(accountNumber);


        return ResponseEntity.ok(ApiResponseFactory.success(
                "Freeze Account  successfully ",
                null,
                HttpStatus.OK,
                request

        ));
    }


    //===========================================================
    // UNFREEZE ACCOUNT
    //===========================================================

    @PatchMapping("/{accountNumber}/unfreeze")
    public ResponseEntity<ApiResponse<Void>> unfreezeAccount(@PathVariable String accountNumber,
            HttpServletRequest request) {


        accountService.unfreezeAccount(accountNumber);


        return ResponseEntity.ok(ApiResponseFactory.success(
                "Account unfrozen successfully",
                null,
                HttpStatus.OK,
                request

        ));
    }


    //===========================================================
    // CLOSE ACCOUNT
    //===========================================================

    @PatchMapping("/{accountNumber}/close")
    public ResponseEntity<ApiResponse<Void>> closeAccount(@PathVariable String accountNumber,
            HttpServletRequest request) {


        accountService.closeAccount(accountNumber);


        return ResponseEntity.ok(ApiResponseFactory.success(
                "Account Closed successfully",
                null,
                HttpStatus.OK,
                request));
    }


    //===========================================================
    // MARK ACCOUNT DORMANT
    //===========================================================

    @PatchMapping("/{accountNumber}/dormant")
    public ResponseEntity<ApiResponse<Void>> markAccountDormant(@PathVariable String accountNumber,
            HttpServletRequest request) {


        accountService.markAccountDormant(accountNumber);


        return ResponseEntity.ok(ApiResponseFactory.success(
                "Account marked dormant successfully",
                null,
                HttpStatus.OK,
                request

        ));
    }


    //===========================================================
    // REACTIVATE ACCOUNT
    //===========================================================

    @PatchMapping("/{accountNumber}/reactivate")
    public ResponseEntity<ApiResponse<Void>> reactivateAccount(@PathVariable String accountNumber,
            HttpServletRequest request) {


        accountService.reactivateAccount(accountNumber);


        return ResponseEntity.ok(ApiResponseFactory.success(
                "Account Reactivated  successfully",
                null,
                HttpStatus.OK,
                request

        ));
    }


}