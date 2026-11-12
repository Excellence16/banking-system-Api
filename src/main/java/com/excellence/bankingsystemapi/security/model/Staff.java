package com.excellence.bankingsystemapi.security.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true, updatable = false, length = 20)
    private String staffNumber;

    @Column(nullable = false)
    private String department;


    @Column(nullable = false, updatable = false)
    private LocalDate dateEmployed;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
