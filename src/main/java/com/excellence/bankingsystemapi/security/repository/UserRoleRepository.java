package com.excellence.bankingsystemapi.security.repository;


import com.excellence.bankingsystemapi.security.model.User;
import com.excellence.bankingsystemapi.security.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUser(User user);

}
