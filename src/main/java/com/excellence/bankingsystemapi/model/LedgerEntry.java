package com.excellence.bankingsystemapi.model;
import com.excellence.bankingsystemapi.common.enums.EntryType;
import  jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_entries",
indexes = {
        @Index(name = "idx_ledger_account",columnList = "account_id"),
        @Index(name = "idx_ledger_transaction",columnList = "transaction_id"),
        @Index(name = "idx_ledger_created_at",columnList = "created_at")
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(nullable = false, precision = 19, scale =2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryType entryType;

    @Column(nullable = false,precision = 19,scale = 2)
    private BigDecimal balanceBefore;

    @Column(nullable = false, precision = 19, scale =2)
    private BigDecimal balanceAfter;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

    }
}