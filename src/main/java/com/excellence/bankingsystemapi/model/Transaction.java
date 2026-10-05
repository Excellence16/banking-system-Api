package com.excellence.bankingsystemapi.model;

import com.excellence.bankingsystemapi.common.enums.Currency;
import com.excellence.bankingsystemapi.common.enums.TransactionChannel;
import com.excellence.bankingsystemapi.common.enums.TransactionStatus;
import com.excellence.bankingsystemapi.common.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "transactions", indexes = {@Index(name = "idx_transaction_status", columnList = "status"), @Index(name = "idx_transaction_reference", columnList = "transaction_reference"), @Index(name = "idx_source_account_date", columnList = "source_account_id, created_at"), @Index(name = "idx_destination_account_date", columnList = "destination_account_id, created_at")


})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID transactionId;

    @Column(nullable = false, unique = true)
    private String transactionReference;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private TransactionChannel channel;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    private String failureReason;

    private String senderNameSnapshot;

    private String senderAccountSnapshot;

    private String senderBankSnapshot;

    private String receiverBankSnapshot;


    private String receiverNameSnapshot;


    private String receiverAccountSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_transaction_id")
    private Transaction originalTransaction;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceBefore;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;


    @Column(length = 255)
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "transaction")
    private List<LedgerEntry> ledgerEntries = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private LocalDateTime postedAt;

    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreated() {

        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.transactionId == null) {
            this.transactionId = UUID.randomUUID();
        }

        if (this.status == null) {
            this.status = TransactionStatus.PENDING;

        }


    }


    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
