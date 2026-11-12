package com.excellence.bankingsystemapi.security.config;


import com.excellence.bankingsystemapi.security.model.Permission;
import com.excellence.bankingsystemapi.security.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {

        createPermissionIfNotExists("CREATE_CUSTOMER");
        createPermissionIfNotExists("VIEW_CUSTOMER");
        createPermissionIfNotExists("UPDATE_CUSTOMER");
        createPermissionIfNotExists("DELETE_CUSTOMER");


        createPermissionIfNotExists("CREATE_ACCOUNT");
        createPermissionIfNotExists("VIEW_ACCOUNT");
        createPermissionIfNotExists("UPDATE_ACCOUNT");
        createPermissionIfNotExists("FREEZE_ACCOUNT");
        createPermissionIfNotExists("CLOSE_ACCOUNT");


        createPermissionIfNotExists("DEPOSIT_FUNDS");
        createPermissionIfNotExists("WITHDRAW_FUNDS");
        createPermissionIfNotExists("TRANSFER_FUNDS");
        createPermissionIfNotExists("REVERSE_TRANSACTION");
        createPermissionIfNotExists("VIEW_TRANSACTIONS");


        createPermissionIfNotExists("MANAGE_USERS");
        createPermissionIfNotExists("MANAGE_ROLES");
        createPermissionIfNotExists("MANAGE_PERMISSIONS");


        createPermissionIfNotExists("VIEW_AUDIT_LOGS");


    }


    private void createPermissionIfNotExists(String name) {
        if (!permissionRepository.existsByName(name)) {

            Permission permission = new Permission();

            permission.setName(name);

            permissionRepository.save(permission);
        }
    }
}
