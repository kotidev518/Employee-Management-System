package com.practice.employee_ms.repo;

import com.practice.employee_ms.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role,Integer> {

    Optional<Role> findByRole(String role);
}
