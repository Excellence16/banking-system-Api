package com.excellence.bankingsystemapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



@Entity
@Table(name = "transactions")
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

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private String description;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<LedgerEntry> ledgerEntries;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void OnCreated() {
            this.createdAt = LocalDateTime.now();
            if(this.status ==null) this.status = TransactionStatus.PENDING;
    }
}
