package com.practice.employee_ms.config;

import com.practice.employee_ms.model.Role;
import com.practice.employee_ms.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        seedRole("USER_ROLE");
        seedRole("ADMIN_ROLE");
    }

    private void seedRole(String roleName) {
        if (roleRepository.findByRole(roleName).isEmpty()) {
            Role role = new Role();
            role.setRole(roleName);
            roleRepository.save(role);
        }
    }
}