package com.excellence.bankingsystemapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "transactions",
       indexes = {
               @Index(name = "idx_transaction_status", columnList = "status")
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

    @Column(nullable = false,unique = true)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

   @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "transaction")
    private List<LedgerEntry> ledgerEntries  = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private  LocalDateTime updatedAt;


    @PrePersist
    protected void OnCreated() {

        LocalDateTime now = LocalDateTime.now();
            this.createdAt = now;
           this.updatedAt = now;
            if(this.status ==null) {
                this.status = TransactionStatus.PENDING;
            }
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
