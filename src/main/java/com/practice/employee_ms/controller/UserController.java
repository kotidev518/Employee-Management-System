package com.practice.employee_ms.controller;

import com.practice.employee_ms.model.User;
import com.practice.employee_ms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<String> SignUp(@RequestBody User user){
        String msg= userService.signup(user);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/login")
    public String SignIn(@RequestBody User user){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                         user.getPassword())
        );
        System.out.println(authentication.getPrincipal());
        return "Login Successful";
    }

    
}
