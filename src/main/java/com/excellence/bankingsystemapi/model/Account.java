package com.excellence.bankingsystemapi.model;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;




@Entity
@Table(name = "accounts",
       indexes = {
         @Index(name = "idx_customer_id", columnList = "customer_id")
       })
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

    @Column(nullable=false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

   @Column(nullable = false, length = 3)
    private  String currency;

    @Version
    private Long version;

   @OneToMany(mappedBy = "account")
   @Builder.Default
    private List<LedgerEntry> ledgerEntries = new ArrayList<>();



   @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

   @Column(nullable = false)
   private LocalDateTime updatedAt;

   @PrePersist
    protected void onCreate() {

        LocalDateTime now =  LocalDateTime.now();
       this.createdAt = now;
       this.updatedAt = now;


       if(this.status ==null) {
           this.status = AccountStatus.ACTIVE;
       }
       if(this.currency == null ) {
           this.currency = "NGN";
       }
   }

   @PreUpdate
    protected void onUpdate() {

       this.updatedAt = LocalDateTime.now();
   }


}
