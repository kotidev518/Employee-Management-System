package com.practice.employee_ms.service;

import com.practice.employee_ms.dto.auth.RegisterRequest;
import com.practice.employee_ms.exception.DuplicateResourceException;
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
    public String signUp(RegisterRequest request) {

        if((repo.findByUsername(request.getUsername())).isPresent()) {
            throw new DuplicateResourceException("Username already Exists");
        }

        if((repo.findByEmail(request.getEmail())).isPresent()){
            throw new DuplicateResourceException("Email already exists");
        }
        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        Role role= roleRepository.findByRole("ROLE_USER")
                .orElseThrow(
                        ()->new IllegalStateException("Default role USER_ROLE not found")
                );
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(role);
        repo.save(user);

        return "User Registered Successfully";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user= repo.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Username not Found"));

        return toUserDetails(user);
    }

    private UserDetails toUserDetails(User user) {
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