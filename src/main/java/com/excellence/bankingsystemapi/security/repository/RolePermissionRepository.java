package com.excellence.bankingsystemapi.security.repository;


import com.excellence.bankingsystemapi.security.model.Permission;
import com.excellence.bankingsystemapi.security.model.Role;
import com.excellence.bankingsystemapi.security.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRole(Role role);

    
    boolean existsByRoleAndPermission(Role role, Permission permission);

}