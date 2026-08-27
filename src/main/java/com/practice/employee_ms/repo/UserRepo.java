package com.practice.employee_ms.repo;

import com.practice.employee_ms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {

    Optional<User> findByusername(String name);
}
