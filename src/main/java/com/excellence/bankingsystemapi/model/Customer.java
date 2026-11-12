package com.excellence.bankingsystemapi.model;

import com.excellence.bankingsystemapi.common.enums.CustomerStatus;
import com.excellence.bankingsystemapi.common.enums.Gender;
import com.excellence.bankingsystemapi.common.enums.KycLevel;
import com.excellence.bankingsystemapi.security.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers", indexes = {@Index(name = "idx_customer_bvn", columnList = "bvn"), @Index(name = "idx_customer_email", columnList = "email"), @Index(name = "idx_customer_phone", columnList = "phone_number"), @Index(name = "idx_customer_number", columnList = "customer_number"), @Index(name = "idx_customer_status", columnList = "status"), @Index(name = "idx_customer_created_at", columnList = "created_at")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 20)
    private String customerNumber;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(length = 100)
    private String middleName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 11)
    private String bvn;

    @Column(unique = true, length = 30)
    private String nationalId;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Column(nullable = false, length = 100)
    private String nationality;

    @Column(nullable = false, length = 300)
    private String residentialAddress;

    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;


    //NO LONGER IN USE !!!

    // @Column(nullable = false, unique = true, length = 150)
    // private String email;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private KycLevel kycLevel = KycLevel.LEVEL_1;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CustomerStatus status = CustomerStatus.ACTIVE;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    //soft delete flag
    @Builder.Default
    @Column(nullable = false)
    private Boolean deleted = false;


    @PrePersist
    private void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (deleted == null) {
            deleted = false;
        }

    }

    @PreUpdate
    private void preUpdate() {

        updatedAt = LocalDateTime.now();


    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}