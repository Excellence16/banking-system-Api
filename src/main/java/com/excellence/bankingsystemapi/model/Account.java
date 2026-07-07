package com.excellence.bankingsystemapi.model;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;




@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;


    @Enumerated(EnumType.STRING)
    @Column(nullable  = false)
    private AccountStatus status;

   @Column(nullable = false)
    private  String currency;

    @Version
    private Long version;

   @OneToMany(mappedBy = "account")
    private List<LedgerEntry> ledgerEntries;

   @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

   @PrePersist
    protected void onCreate() {
       this.createdAt = LocalDateTime.now();
       if(this.status ==null) this.status =AccountStatus.ACTIVE;
       if(this.currency == null ) this.currency = "NGN";
   }


}
