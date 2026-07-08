package com.excellence.bankingsystemapi.repository;


import com.excellence.bankingsystemapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository <Customer,  Long>{

    Optional<Customer> findByEmail(String email);


    Optional<Customer> findByBvn(String bvn);


    boolean existsByEmail(String email);


    boolean existsByBvn(String bvn);
}
