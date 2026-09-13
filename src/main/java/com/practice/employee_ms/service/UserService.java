package com.practice.employee_ms.service;

import com.practice.employee_ms.model.Role;
import com.practice.employee_ms.model.User;
import com.practice.employee_ms.repo.RoleRepository;
import com.practice.employee_ms.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public String signup(User user) {

        Role role= roleRepository.findByRole("ROLE_USER").orElseThrow(()->new IllegalStateException("Default role USER_ROLE not found"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(role);
        repo.save(user);

        return "User Registered Successfully";
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user= repo.findByusername(username)
                .orElseThrow(()->new RuntimeException("Username not Found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        user.getRoles()
                                .stream()
                                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                                .toList()
                )
                .build();
    }
}
