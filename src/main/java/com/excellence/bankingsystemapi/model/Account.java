package com.excellence.bankingsystemapi.model;

import com.excellence.bankingsystemapi.common.enums.AccountStatus;
import com.excellence.bankingsystemapi.common.enums.AccountType;
import com.excellence.bankingsystemapi.common.enums.Currency;
import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;




@Entity
@Table(name = "accounts",
       indexes = {
               @Index(name ="idx_customer_id", columnList =" customer_id "),
               @Index(name = "idx_account_type", columnList = "account_type"),
               @Index(name = "idx_account_status", columnList = "account_status")

       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    /**
     * PRIMARY KEY.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * UNIQUE ACCOUNT NUMBER.
     * GENERATED ONCE DURING ACCOUNT CREATION.
     */
    @Column(name = "account_number", nullable = false, unique = true,length = 10)
    private String accountNumber;

    /**
     * HUMAN-READABLE ACCOUNT NAME.
     * EXAMPLE;
     * KELVIN EMMA SAVINGS
     */
    @Column(name = "account_name", nullable = false,length = 100)
     private String accountName;

    /**
     * OWNER OF THIS ACCOUNT.
     * ONE CUSTOMER MAY OWN MULTIPLE ACCOUNTS.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    /**
     * SAVINGS / CURRENT .
     */
    @Enumerated(EnumType.STRING)
    @Column(name="account_type", nullable = false)
    private AccountType accountType;


    /**
     * ACTIVE,
     * FROZEN,
     * CLOSED.
     * ETC.
     */
    @Enumerated(EnumType.STRING)
    @Column(name="account_status", nullable  = false)
    private AccountStatus status;

    /**
     * ACTUAL LEDGER BALANCE.
     */
    @Column(name = "ledger_balance" ,   nullable=false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal ledgerBalance = BigDecimal.ZERO;

    /**
     * SPENDABLE BALANCE.
     */
    @Column(name = "available_balance" ,   nullable=false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal availableBalance = BigDecimal.ZERO;


    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    /**
     * NGN,
     * USD,
     * EUR
     */
    @Enumerated(EnumType.STRING)
   @Column(name = "currency",  nullable = false, length = 3)
    private Currency currency;

    /**
     * OPTIMISTIC LOCKING.
     */
    @Version
    private Long version;

   @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
   @Builder.Default
    private List<LedgerEntry> ledgerEntries = new ArrayList<>();

    /**
     * AUDIT FIELDS.
     */

   @Column(name = "created_at",  nullable = false, updatable = false)
    private LocalDateTime createdAt;

   @Column(name = "updated_at",nullable = false)
   private LocalDateTime updatedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;


   @PrePersist
    protected void onCreate() {

        LocalDateTime now =  LocalDateTime.now();
       this.updatedAt = now;
       this.createdAt = now;


       if (status == null) {
           status = AccountStatus.ACTIVE;
       }
       if(currency == null ) {
           currency = Currency.NGN;
       }
   }

   @PreUpdate
    protected void onUpdate() {

       this.updatedAt = LocalDateTime.now();
   }


}
