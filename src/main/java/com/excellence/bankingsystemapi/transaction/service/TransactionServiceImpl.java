package com.excellence.bankingsystemapi.transaction.service;

import com.excellence.bankingsystemapi.account.repository.AccountRepository;
import com.excellence.bankingsystemapi.common.enums.AccountStatus;
import com.excellence.bankingsystemapi.common.enums.EntryType;
import com.excellence.bankingsystemapi.common.enums.TransactionStatus;
import com.excellence.bankingsystemapi.common.enums.TransactionType;
import com.excellence.bankingsystemapi.exceptions.account.AccountNotActiveException;
import com.excellence.bankingsystemapi.exceptions.account.InsufficientBalanceException;
import com.excellence.bankingsystemapi.exceptions.global.InvalidRequestException;
import com.excellence.bankingsystemapi.exceptions.global.ResourceNotFoundException;
import com.excellence.bankingsystemapi.ledger.repository.LedgerEntryRepository;
import com.excellence.bankingsystemapi.model.Account;
import com.excellence.bankingsystemapi.model.LedgerEntry;
import com.excellence.bankingsystemapi.model.Transaction;
import com.excellence.bankingsystemapi.transaction.dto.request.DepositRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.request.ReverseTransactionRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.request.TransferRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.request.WithdrawalRequestDTO;
import com.excellence.bankingsystemapi.transaction.dto.response.TransactionResponseDTO;
import com.excellence.bankingsystemapi.transaction.mapper.TransactionMapper;
import com.excellence.bankingsystemapi.transaction.repository.TransactionRepository;
import com.excellence.bankingsystemapi.util.TransactionReferenceGenerator;
import com.excellence.bankingsystemapi.util.UniqueCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private static final long REVERSAL_PERIOD_HOURS = 48;

    @Override
    @Transactional
    public TransactionResponseDTO deposit(DepositRequestDTO request) {


        Account account = getValidatedDestinationAccount(request.accountNumber());


        validateAccountForDeposit(account);


        BigDecimal balanceBefore = account.getLedgerBalance();


        creditAccount(account, request.amount());


        String reference = generateTransactionReference();


        Transaction transaction = buildDepositTransaction(request,
                account,
                balanceBefore,
                reference);


        transactionRepository.save(transaction);


        LedgerEntry ledgerEntry = buildDepositLedgerEntry(transaction,
                account,
                balanceBefore,
                request);


        ledgerEntryRepository.save(ledgerEntry);


        return transactionMapper.toResponseDTO(transaction);
    }


    @Override
    @Transactional
    public TransactionResponseDTO withdraw(WithdrawalRequestDTO request) {


        log.info("Processing withdrawal of {} from account {}",
                request.amount(),
                request.accountNumber());


        Account sourceAccount = getValidatedSourceAccount(request.accountNumber());


        validateAccountForWithdrawal(sourceAccount);


        validateSufficientBalance(sourceAccount, request.amount());


        BigDecimal balanceBefore = sourceAccount.getLedgerBalance();


        debitAccount(sourceAccount, request.amount());


        String transactionReference = generateTransactionReference();


        Transaction transaction = Transaction
                .builder()
                .transactionReference(transactionReference)
                .transactionType(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.SUCCESS)
                .channel(request.channel())
                .currency(sourceAccount.getCurrency())
                .amount(request.amount())
                .description(request.description())
                .sourceAccount(sourceAccount)
                .balanceBefore(balanceBefore)
                .balanceAfter(sourceAccount.getLedgerBalance())
                .build();


        transactionRepository.save(transaction);


        LedgerEntry ledgerEntry = LedgerEntry
                .builder()
                .transaction(transaction)
                .account(sourceAccount)
                .entryType(EntryType.DEBIT)
                .currency(sourceAccount.getCurrency())
                .amount(request.amount())
                .balanceBefore(balanceBefore)
                .balanceAfter(sourceAccount.getLedgerBalance())
                .description(request.description())
                .build();


        ledgerEntryRepository.save(ledgerEntry);


        return transactionMapper.toResponseDTO(transaction);
    }


    @Override
    @Transactional
    public TransactionResponseDTO transfer(TransferRequestDTO request) {


        log.info("Processing transfer of {} from {} to {}",
                request.amount(),
                request.sourceAccountNumber(),
                request.destinationAccountNumber());


        Account sourceAccount = getValidatedSourceAccount(request.sourceAccountNumber());


        Account destinationAccount = getValidatedDestinationAccount(request.destinationAccountNumber());


        validateAccountForWithdrawal(sourceAccount);


        validateAccountForDeposit(destinationAccount);


        validateDifferentAccounts(sourceAccount, destinationAccount);


        validateSufficientBalance(sourceAccount, request.amount());


        BigDecimal sourceBalanceBefore = sourceAccount.getLedgerBalance();


        BigDecimal destinationBalanceBefore = destinationAccount.getLedgerBalance();


        debitAccount(sourceAccount, request.amount());


        creditAccount(destinationAccount, request.amount());


        String transactionReference = generateTransactionReference();


        Transaction transaction = buildTransferTransaction(request,
                sourceAccount,
                destinationAccount,
                sourceBalanceBefore,
                transactionReference);


        transactionRepository.save(transaction);


        LedgerEntry debitEntry = buildDebitLedgerEntry(transaction,
                sourceAccount,
                request.amount(),
                sourceBalanceBefore,
                request.description()

        );


        LedgerEntry creditEntry = buildCreditLedgerEntry(transaction,
                destinationAccount,
                request.amount(),
                destinationBalanceBefore,
                request.description());


        ledgerEntryRepository.save(debitEntry);

        ledgerEntryRepository.save(creditEntry);


        return transactionMapper.toResponseDTO(transaction);


    }

    @Override
    @Transactional
    public TransactionResponseDTO reverseTransaction(String transactionReference,
            ReverseTransactionRequestDTO requestDTO) {

        Transaction originalTransaction = getValidatedTransactionForReversal(
                transactionReference);

        validateTransactionForReversal(originalTransaction);

        Transaction reversalTransaction = switch (originalTransaction.getTransactionType()) {

            case DEPOSIT ->
                    reverseDeposit(originalTransaction, requestDTO.reason());

            case WITHDRAWAL ->
                    reverseWithdrawal(originalTransaction, requestDTO.reason());

            case TRANSFER ->
                    reverseTransfer(originalTransaction, requestDTO.reason());

            default -> throw new InvalidRequestException(
                    "Unsupported transaction type.");
        };

        return transactionMapper.toResponseDTO(reversalTransaction);
    }


    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDTO getTransactionByReference(String transactionReference) {

        Transaction transaction = transactionRepository
                .findByTransactionReference(transactionReference)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found"));


        return transactionMapper.toResponseDTO(transaction);
    }

    //HELPER METHODS  FOR DEPOSIT//


    // GET AND VALIDATE DESTINATION ACCOUNT //

    private Account getValidatedDestinationAccount(String accountNumber) {
        return accountRepository
                .findByAccountNumberAndDeletedFalse(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        " Account number not found "));
    }


    // VALIDATE THAT THE ACCOUNT CAN RECEIVE MONEY ..YOU CHECK IF THE ACCOUNT IS ELIGIBLE TO RECEIVE MONEY .
    // IF ACTIVE != ACTIVE, WHICH IS FALSE , THE ACCOUNT IS ELIGIBLE
    private void validateAccountForDeposit(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException();
        }
    }

    // CREDIT THE ACCOUNT ...YOU PASS THE DESTINATION ACCOUNT AND THE AMOUNT THAT THE DEPOSITOR WANTS TO PAY-IN

    private void creditAccount(Account account, BigDecimal amount) {
        account.setLedgerBalance(account.getLedgerBalance().add(amount));
        account.setAvailableBalance(account.getAvailableBalance().add(amount));
    }

    //GENERATE TRANSACTION REFERENCE
    private String generateTransactionReference() {
        return UniqueCodeGenerator.generateUnique(TransactionReferenceGenerator::generate,
                transactionRepository::existsByTransactionReference);

    }

    private Transaction buildDepositTransaction(DepositRequestDTO requestDTO,
            Account account,
            BigDecimal balanceBefore,
            String reference) {
        return Transaction
                .builder()
                .transactionReference(reference)
                .transactionType(TransactionType.DEPOSIT)
                .status(TransactionStatus.SUCCESS)
                .channel(requestDTO.channel())
                .currency(account.getCurrency())
                .amount(requestDTO.amount())
                .description(requestDTO.description())
                .destinationAccount(account)
                .balanceBefore(balanceBefore)
                .balanceAfter(account.getLedgerBalance())
                .build();
    }


    private LedgerEntry buildDepositLedgerEntry(Transaction transaction,
            Account account,
            BigDecimal balanceBefore,
            DepositRequestDTO request) {

        return LedgerEntry
                .builder()
                .transaction(transaction)
                .account(account)
                .entryType(EntryType.CREDIT)
                .amount(request.amount())
                .currency(account.getCurrency())
                .balanceAfter(account.getLedgerBalance())
                .balanceBefore(balanceBefore)
                .description(request.description())
                .build();
    }


    //HELPER METHODS FOR WITHDRAWAL ..

    private Account getValidatedSourceAccount(String accountNumber) {
        return accountRepository
                .findByAccountNumberAndDeletedFalse(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "source account not found "));
    }


    private void validateAccountForWithdrawal(Account account) {

        if (account.getStatus() != AccountStatus.ACTIVE) {

            throw new AccountNotActiveException();
        }
    }

//HELPER METHODS FOR TRANSFER .

    private void validateDifferentAccounts(Account source,
            Account destination) {
        if (source.getId().equals(destination.getId())) {
            throw new InvalidRequestException("Cannot transfer to same account");
        }
    }

    private void validateSufficientBalance(Account account, BigDecimal amount) {

        if (account.getAvailableBalance().compareTo(amount) < 0) {


            throw new InsufficientBalanceException("Insufficient balance");
        }
    }


    private void debitAccount(Account account, BigDecimal amount) {

        account.setLedgerBalance(account.getLedgerBalance().subtract(amount));


        account.setAvailableBalance(account
                .getAvailableBalance()
                .subtract(amount));
    }

    private Transaction buildTransferTransaction(TransferRequestDTO request,
            Account sourceAccount,
            Account destinationAccount,
            BigDecimal sourceBalanceBefore,
            String reference) {


        return Transaction.builder()

                .transactionReference(reference)

                .transactionType(TransactionType.TRANSFER)

                .status(TransactionStatus.SUCCESS)

                .channel(request.channel())

                .currency(sourceAccount.getCurrency())

                .amount(request.amount())

                .description(request.description())

                .sourceAccount(sourceAccount)

                .destinationAccount(destinationAccount)

                .balanceBefore(sourceBalanceBefore)


                .balanceAfter(sourceAccount.getLedgerBalance())

                .build();
    }

    //   HELPER METHOD FOR BUILDING A LEDGER ENTRY DEBIT TRANSACTION.
    private LedgerEntry buildDebitLedgerEntry(Transaction transaction,
            Account account,
            BigDecimal amount,
            BigDecimal balanceBefore,
            String description) {


        return LedgerEntry.builder()

                .transaction(transaction)

                .account(account).currency(account.getCurrency())

                .entryType(EntryType.DEBIT)

                .amount(amount).balanceBefore(balanceBefore)

                .balanceAfter(account.getLedgerBalance())

                .description(description)

                .build();
    }

    //HELPER METHODS FOR BUILDING A CREDIT TRANSFER ENTITY TRANSACTION OBJECT //
    private LedgerEntry buildCreditLedgerEntry(Transaction transaction,
            Account account,
            BigDecimal amount,
            BigDecimal balanceBefore,
            String description) {


        return LedgerEntry.builder()

                .transaction(transaction)

                .account(account)

                .entryType(EntryType.CREDIT)

                .amount(amount).currency(account.getCurrency())

                .balanceBefore(balanceBefore)


                .balanceAfter(account.getLedgerBalance())


                .description(description)

                .build();
    }


    //HELPER METHODS FOR REVERSAL

    //1ST STEP...LOAD OR GET THE TRANSACTION FOR REVERSAL USING THE TRANSACTION REFERENCE
    private Transaction getValidatedTransactionForReversal(String transactionReference) {

        return transactionRepository
                .findByTransactionReference(transactionReference)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found."));

    }

// AT THIS POINT , THE TRANSACTION OBJECT HAS BEEN RECOVERED FROM THE DB...

    // NOW WE HAVE THE TRANSACTION OBJECT IN MEMORY WE CHECK IF THE TRANSACTION IS VALID FOR REVERSAL .
    //1ST STEP IN CHECKING :  CHECK IF THE TRANSACTION WAS SUCCESSFUL (ONLY SUCCESSFUL TRANSACTIONS CAN BE REVERSED .
    // WHY ?BECAUSE A FAILED TRANSACTION FAILED AND MONEY WASN'T LOST //

    private void validateTransactionForReversal(Transaction transaction) {
        if (transaction.getStatus() != TransactionStatus.SUCCESS) {

            throw new InvalidRequestException(

                    "Only successful transactions can be reversed.");
        }


        // 2ND CHECK: IF THE TRANSACTION ITSELF IS A REVERSAL TRANSACTION .
        // WHY? BECAUSE YOU CANT REVERSED A REVERSAL TRANSACTION //

        if (transaction.getTransactionType() == TransactionType.REVERSAL) {

            throw new InvalidRequestException(
                    "A reversal transaction cannot be reversed.");

        }
        if (transactionRepository.existsByOriginalTransactionAndTransactionType(
                transaction,
                TransactionType.REVERSAL)) {
            throw new InvalidRequestException(
                    "Transaction has already been reversed.");
        }


        if (transaction
                .getCreatedAt()
                .isBefore(LocalDateTime
                        .now()
                        .minusHours(REVERSAL_PERIOD_HOURS))) {

            throw new InvalidRequestException(
                    "Transaction reversal period has expired.");
        }
    }

// HELPER METHOD TO REVERS A DEPOSIT TRANSACTION...

    // HELPER METHOD TO REVERSE A DEPOSIT TRANSACTION

    private Transaction reverseDeposit(Transaction originalTransaction,
            String reason) {

        Account destinationAccount = originalTransaction.getDestinationAccount();


        BigDecimal reversalAmount = originalTransaction.getAmount();


        if (destinationAccount
                .getAvailableBalance()
                .compareTo(reversalAmount) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient available balance for reversal."

            );
        }


        BigDecimal balanceBefore = destinationAccount.getLedgerBalance();


        debitAccount(destinationAccount, reversalAmount);


        String reference = generateTransactionReference();


        Transaction reversalTransaction = buildReversalTransaction(
                originalTransaction,
                destinationAccount,
                reversalAmount,
                balanceBefore,
                reference,
                reason);


        transactionRepository.save(reversalTransaction);


        LedgerEntry ledgerEntry = buildReversalLedgerEntry(reversalTransaction,
                destinationAccount,
                reversalAmount,
                balanceBefore,
                reason);


        ledgerEntryRepository.save(ledgerEntry);


        return reversalTransaction;
    }


    // BUILDS THE REVERSAL TRANSACTION OBJECT FOR DEPOSIT
    private Transaction buildReversalTransaction(Transaction originalTransaction,
            Account destinationAccount,
            BigDecimal reversalAmount,
            BigDecimal balanceBefore,
            String reference,
            String reason) {

        return Transaction
                .builder()
                .transactionReference(reference)
                .transactionType(TransactionType.REVERSAL)
                .status(TransactionStatus.SUCCESS)
                .amount(reversalAmount)
                .originalTransaction(originalTransaction)
                .destinationAccount(destinationAccount)
                .balanceBefore(balanceBefore)
                .balanceAfter(destinationAccount.getLedgerBalance())
                .description("Reversal of transaction " + originalTransaction.getTransactionReference() + ". Reason: " + reason)
                .build();
    }


    // BUILDS THE REVERSAL LEDGER ENTRY OBJECT FOR DEPOSIT
    private LedgerEntry buildReversalLedgerEntry(Transaction reversalTransaction,
            Account destinationAccount,
            BigDecimal reversalAmount,
            BigDecimal balanceBefore,
            String reason) {

        return LedgerEntry
                .builder()
                .transaction(reversalTransaction)
                .account(destinationAccount)
                .entryType(EntryType.DEBIT)
                .currency(destinationAccount.getCurrency())
                .amount(reversalAmount)
                .balanceAfter(destinationAccount.getLedgerBalance())
                .balanceBefore(balanceBefore)
                .description("Reversal of transaction " + reversalTransaction
                        .getOriginalTransaction()
                        .getTransactionReference() + ". Reason: " + reason)
                .build();


    }

    private Transaction reverseWithdrawal(Transaction originalTransaction,
            String reason) {

        Account sourceAccount = originalTransaction.getSourceAccount();

        BigDecimal reversalAmount = originalTransaction.getAmount();

        BigDecimal balanceBefore = sourceAccount.getLedgerBalance();

        creditAccount(sourceAccount, reversalAmount);

        String reference = generateTransactionReference();


        // BUILD WITHDRAWAL REVERSE TRANSACTION FOR WITHDRAWAL.

        Transaction reversalTransaction = Transaction
                .builder()
                .transactionReference(reference)
                .transactionType(TransactionType.REVERSAL)
                .status(TransactionStatus.SUCCESS)
                .amount(reversalAmount)
                .originalTransaction(originalTransaction)
                .sourceAccount(sourceAccount)
                .balanceBefore(balanceBefore)
                .balanceAfter(sourceAccount.getLedgerBalance())
                .description("Reversal of transaction " + originalTransaction.getTransactionReference() + ". Reason: " + reason)

                .build();
/**
 *
 *
 LedgerEntry reversalLedgerEntry =
 buildReversalLedgerEntry(
 reversalTransaction,
 sourceAccount,
 reversalAmount,
 reason
 );
 */

        transactionRepository.save(reversalTransaction);


        LedgerEntry reversalLedgerEntry = LedgerEntry
                .builder()

                .transaction(reversalTransaction)

                .account(sourceAccount)

                .entryType(EntryType.CREDIT)

                .amount(reversalAmount)
                .currency(sourceAccount.getCurrency())

                .balanceAfter(sourceAccount.getLedgerBalance())
                .balanceBefore(balanceBefore)


                .description("Reversal of transaction " + originalTransaction.getTransactionReference() + ". Reason: " + reason)

                .build();


        ledgerEntryRepository.save(reversalLedgerEntry);

        return reversalTransaction;
    }

    //HELPER METHOD FOR BUILDING A REVERSE WITHDRAWAL TRANSACTION

    /**
     * RESERVED FOR THE DELEGATION METHOD ..THE METHOD CALL .
     */


// HELPER METHOD FOR REVERSING A TRANSFER TRANSACTION :
    private Transaction reverseTransfer(Transaction originalTransaction,
            String reason) {

        /*
         * Keep the original transaction relationship.
         *
         * Source:
         * Mr A
         *
         * Destination:
         * Mr B
         *
         * We are not swapping them here because
         * the transaction history should remain clear.
         */
        Account originalSourceAccount = originalTransaction.getSourceAccount();

        Account originalDestinationAccount = originalTransaction.getDestinationAccount();


        /*
         * The amount to return back.
         */
        BigDecimal reversalAmount = originalTransaction.getAmount();


        /*
         * The money currently sits in the destination account.
         *
         * Before we remove it, confirm it is still available.
         */
        if (originalDestinationAccount
                .getAvailableBalance()
                .compareTo(reversalAmount) < 0) {

            throw new InsufficientBalanceException(
                    "Destination account does not have enough balance for reversal.");
        }


        /*
         * Capture balances before reversal.
         *
         * Since the reversal is taking money from
         * the destination account, our transaction
         * balance snapshot represents that account.
         */
        BigDecimal destinationBalanceBefore = originalDestinationAccount.getLedgerBalance();


        /*
         * We still capture source balance because
         * we may need it for future auditing/logging.
         */
        BigDecimal sourceBalanceBefore = originalSourceAccount.getLedgerBalance();



        /*
         * Reverse the actual money movement.
         *
         * Original:
         *
         * SOURCE ---------> DESTINATION
         *
         * Reversal:
         *
         * DESTINATION -----> SOURCE
         */
        debitAccount(originalDestinationAccount, reversalAmount);


        creditAccount(originalSourceAccount, reversalAmount);



        /*
         * Create a new transaction record.
         *
         * This is NOT updating the old transaction.
         * A reversal is a new financial event.
         */
        Transaction reversalTransaction = Transaction
                .builder()

                .transactionReference(generateTransactionReference())

                .transactionType(TransactionType.REVERSAL)

                .status(TransactionStatus.SUCCESS)

                .channel(originalTransaction.getChannel())

                .currency(originalTransaction.getCurrency())

                .amount(reversalAmount)


                /*
                 * Keep original relationship
                 */
                .sourceAccount(originalSourceAccount)

                .destinationAccount(originalDestinationAccount)


                /*
                 * Link reversal to original transaction
                 */.originalTransaction(originalTransaction)


                /*
                 * Keep original snapshots
                 */
                .senderNameSnapshot(originalTransaction.getSenderNameSnapshot())

                .senderAccountSnapshot(originalTransaction.getSenderAccountSnapshot())

                .senderBankSnapshot(originalTransaction.getSenderBankSnapshot())


                .receiverNameSnapshot(originalTransaction.getReceiverNameSnapshot())

                .receiverAccountSnapshot(originalTransaction.getReceiverAccountSnapshot())

                .receiverBankSnapshot(originalTransaction.getReceiverBankSnapshot())


                /*
                 * The balance snapshot represents
                 * the account losing money during reversal.
                 */
                .balanceBefore(destinationBalanceBefore)

                .balanceAfter(originalDestinationAccount.getLedgerBalance())


                .description("Reversal of transaction " + originalTransaction.getTransactionReference() + ". Reason: " + reason)

                .build();


        transactionRepository.save(reversalTransaction);



        /*
         * Ledger direction is opposite to original transfer.
         *
         * Original transfer:
         *
         * DEBIT  source
         * CREDIT destination
         *
         *
         * Reversal:
         *
         * DEBIT  destination
         * CREDIT source
         */

        //HELPER METHOD FOR BUILDING A DEBIT REVERSE TRANSFER OBJECT
//N/B:
        // EVERY TRANSFER HAS A SOURCE (HERE CALLED ORIGINALSOURCEACOUNT)
        // THE SOURCE ACCOUNT IS THE ACCOUNT THAT IS SENDING THE MONEY .THEN  AND A DESTINATIONATIONACCOUNT .
        // THE DESTINATION ACCOUNT IS THE ACCOUNT THAT WILL RECEIVE THE  MONEY .
        // AGAIN EVERY TRANSFER MUST RECORD A  DEBIT AND CREDIT LEDGER ENTRY .
        //IN THIS CASE, BECAUSE IT IS A REVERSAL TRANSFER TRANSACTION ,
        // THE DEBIT IS THE DESTINATION ACCOUNT OTHERWISE AS REFERRED OR USED
        // HERE CALLED ORIGINAL DESTINATION ACCOUNT .
        // DURING REVERSAL, THE DESTINATION ACCOUNT WILL BE THE ONE TO SEND
        // MONEY NOW BECAUSE IT IS A REVERSAL TRANSFER .

        LedgerEntry debitEntry = LedgerEntry
                .builder()
                .transaction(reversalTransaction)
                .account(originalDestinationAccount)
                .entryType(EntryType.DEBIT)
                .currency(originalDestinationAccount.getCurrency())
                .amount(reversalAmount)
                .balanceBefore(destinationBalanceBefore)
                .balanceAfter(originalDestinationAccount.getLedgerBalance())
                .description("Reversal of transaction " + originalTransaction.getTransactionReference() + ". Reason: " + reason)
                .build();

        //HELPER METHOD FOR BUILDING A CREDIT REVERSE TRANSACTION OBJECT .
        // N/B:
        // EVERY TRANSFER HAS A SOURCE (HERE CALLED ORIGINALSOURCEACOUNT)
        // THE SOURCE ACCOUNT IS THE ACCOUNT THAT IS SENDING THE MONEY .
        // AND A DESTINATIONATIONACCOUNT . THE DESTINATION ACCOUNT IS THE ACCOUNT THAT WILL RECEIVE THE  MONEY .
        // AGAIN EVERY TRANSFER MUST RECORD A  DEBIT AND CREDIT LEDGER ENTRY .
        //IN THIS CASE, BECAUSE IT IS A REVERSAL TRANSFER TRANSACTION ,
        // THE CREDIT IS THE SOURCEACCOUNT OTHERWISE AS REFERRED OR USED HERE CALLED ORIGINALSOURCEACCOUNT .
        // THE SOURCE ACCOUNT WILL BE THE ONE TO RECEIVE MONEY NOW BECAUSE IT IS A REVERSAL TRANSFER .

        LedgerEntry creditEntry = LedgerEntry
                .builder()
                .transaction(reversalTransaction)
                .account(originalSourceAccount)
                .entryType(EntryType.CREDIT)
                .currency(originalSourceAccount.getCurrency())
                .amount(reversalAmount)
                .balanceBefore(sourceBalanceBefore)
                .balanceAfter(originalSourceAccount.getLedgerBalance())
                .description("Reversal of transaction " + originalTransaction.getTransactionReference() + ". Reason: " + reason)
                .build();


        ledgerEntryRepository.save(debitEntry);


        ledgerEntryRepository.save(creditEntry);


        return reversalTransaction;
    }
}

/***
 *
 * private Transaction reverseDeposit(
 *         Transaction originalTransaction,
 *         String reason
 * ) {
 *
 *
 *     // 1. Get the account affected
 *     Account destinationAccount =
 *             originalTransaction.getDestinationAccount();
 *
 *
 *     // 2. Get amount to reverse
 *     BigDecimal reversalAmount =
 *             originalTransaction.getAmount();
 *
 *
 *     // 3. Check money exists
 *     if (destinationAccount.getAvailableBalance()
 *             .compareTo(reversalAmount) < 0) {
 *
 *         throw new BusinessRuleException(
 *                 "Insufficient available balance for reversal."
 *         );
 *     }
 *
 *
 *     // 4. Capture old balance
 *     BigDecimal balanceBefore =
 *             destinationAccount.getLedgerBalance();
 *
 *
 *     // 5. Reduce customer's balance
 *     debitAccount(destinationAccount, reversalAmount);
 *
 *
 *     // 6. CREATE reversal transaction object
 *     Transaction reversalTransaction =
 *             buildReversalTransaction(...);
 *
 *
 *     // 7. SAVE reversal transaction to database
 *     transactionRepository.save(reversalTransaction);
 *
 *
 *     // 8. CREATE ledger entry object
 *     LedgerEntry ledgerEntry =
 *             buildReversalLedgerEntry(...);
 *
 *
 *     // 9. SAVE ledger entry to database
 *     ledgerEntryRepository.save(ledgerEntry);
 *
 *
 *     return reversalTransaction;
 * }
 */
