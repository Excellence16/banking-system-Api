package com.excellence.bankingsystemapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private  String bvn;

    @OneToMany(mappedBy = "customer" , cascade = CascadeType.ALL)
    private List<Account>accounts;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
