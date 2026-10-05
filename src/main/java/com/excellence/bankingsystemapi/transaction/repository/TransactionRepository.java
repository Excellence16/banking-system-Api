


package com.excellence.bankingsystemapi.transaction.repository;


   import com.excellence.bankingsystemapi.common.enums.TransactionType;
   import com.excellence.bankingsystemapi.model.Transaction;
   import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    Optional<Transaction> findByTransactionReference(String transactionReference);

    boolean existsByTransactionReference(String transactionReference);

    boolean existsByOriginalTransactionAndTransactionType(Transaction transaction, TransactionType transactionType);
}
