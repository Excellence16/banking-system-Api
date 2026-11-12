package com.excellence.bankingsystemapi.security.config;


import com.excellence.bankingsystemapi.security.model.Permission;
import com.excellence.bankingsystemapi.security.model.Role;
import com.excellence.bankingsystemapi.security.model.RolePermission;
import com.excellence.bankingsystemapi.security.repository.PermissionRepository;
import com.excellence.bankingsystemapi.security.repository.RolePermissionRepository;
import com.excellence.bankingsystemapi.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RolePermissionInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;


    @Override
    public void run(String... args) {


        Role customerRole = getRole("CUSTOMER");
        Role tellerRole = getRole("TELLER");
        Role operationsRole = getRole("OPERATIONS_OFFICER");
        Role adminRole = getRole("ADMIN");
        Role auditorRole = getRole("AUDITOR");

        //CUSTOMER PERMISSIONS//

        assign(customerRole, "VIEW_ACCOUNT");
        assign(customerRole, "TRANSFER_FUNDS");
        assign(customerRole, "VIEW_TRANSACTION");


        //TELLER PERMISSIONS//
        assign(tellerRole, "CREATE_CUSTOMER");
        assign(tellerRole, "VIEW_CUSTOMER");
        assign(tellerRole, "CREATE_ACCOUNT");
        assign(tellerRole, "DEPOSIT_FUNDS");
        assign(tellerRole, "WITHDRAW_FUNDS");


        //OPERATIONS OFFICER PERMISSIONS//

        assign(operationsRole, "VIEW_CUSTOMER");
        assign(operationsRole, "UPDATE_CUSTOMER");
        assign(operationsRole, "VIEW_ACCOUNT");
        assign(operationsRole, "UPDATE_ACCOUNT");
        assign(operationsRole, "FREEZE_ACCOUNT");
        assign(operationsRole, "CLOSE_ACCOUNT");
        assign(operationsRole, "REVERSE_TRANSACTION");


        //AUDITOR PERMISSIONS//
        assign(auditorRole, "VIEW_AUDIT_LOGS");
        assign(auditorRole, "VIEW_TRANSACTIONS");
        assign(auditorRole, "VIEW_CUSTOMER");


        //ADMIN PERMISSIONS//
        assign(adminRole, "MANAGE_USERS");
        assign(adminRole, "MANAGE_ROLES");
        assign(adminRole, "MANAGE_PERMISSIONS");


    }

    private Role getRole(String name) {
        return roleRepository
                .findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }

    private void assign(Role role, String permissionName) {

        Permission permission = permissionRepository
                .findByName(permissionName)
                .orElseThrow(() -> new RuntimeException("Permission not " + "found: " + permissionName));


        if (!rolePermissionRepository.existsByRoleAndPermission(role,
                permission)) {

            RolePermission rolePermission = new RolePermission();

            rolePermission.setRole(role);
            rolePermission.setPermission(permission);

            rolePermissionRepository.save(rolePermission);
        }
    }
}
