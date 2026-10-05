package com.excellence.bankingsystemapi.transaction.controller;


import com.excellence.bankingsystemapi.common.ApiResponse;
import com.excellence.bankingsystemapi.common.ApiResponseFactory;
import com.excellence.bankingsystemapi.transaction.dto.request.DepositRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.request.ReverseTransactionRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.request.TransferRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.request.WithdrawalRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.response.TransactionResponseDTO;
import com.excellence.bankingsystemapi.transaction.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> deposit(@Valid @RequestBody DepositRequestDTO requestDTO,
            HttpServletRequest request) {

        TransactionResponseDTO response = transactionService.deposit(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.success("Deposit successful",
                        response,
                        HttpStatus.CREATED,
                        request));
    }


    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> withdraw(@Valid @RequestBody WithdrawalRequestDTO requestDTO,
            HttpServletRequest request) {

        TransactionResponseDTO response = transactionService.withdraw(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.success("Withdrawal successful",
                        response,
                        HttpStatus.CREATED,
                        request));
    }


    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> transfer(@Valid @RequestBody TransferRequestDTO requestDTO,
            HttpServletRequest request) {

        TransactionResponseDTO response = transactionService.transfer(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.success("Transfer successful ",
                        response,
                        HttpStatus.CREATED,
                        request));
    }

    @PostMapping("/{transactionReference}/reverse")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> reverse(@PathVariable String transactionReference,
            @Valid @RequestBody ReverseTransactionRequestDTO requestDTO,
            HttpServletRequest request) {

        TransactionResponseDTO response = transactionService.reverseTransaction(
                transactionReference,
                requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.success(
                        "Transaction reversed successfully",
                        response,
                        HttpStatus.CREATED,
                        request));

    }


    @GetMapping("/{transactionReference}")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> getTransactionByReference(
            @PathVariable String transactionReference,
            HttpServletRequest request) {

        TransactionResponseDTO response = transactionService.getTransactionByReference(
                transactionReference);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success(
                        "Transaction retrieved successfully",
                        response,
                        HttpStatus.OK,
                        request));
    }


}
