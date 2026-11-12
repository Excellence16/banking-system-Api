package com.excellence.bankingsystemapi.security.config;


import com.excellence.bankingsystemapi.security.model.Role;
import com.excellence.bankingsystemapi.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;


    @Override
    public void run(String... args) {
        createRoleIfNotExists("CUSTOMER", " Default customer role");

        createRoleIfNotExists("ADMIN", " System administrator role");

        createRoleIfNotExists("AUDITOR", "Audit and compliance role");

        createRoleIfNotExists("TELLE", "Branch teller role");

        createRoleIfNotExists("OPERATIONS_OFFICER",
                "Bank operations officer " + "role");


    }


    private void createRoleIfNotExists(String name, String description) {
        if (!roleRepository.existsByName(name)) {


            Role role = new Role();

            role.setName(name);
            role.setDescription(description);

            roleRepository.save(role);
        }
    }
}
