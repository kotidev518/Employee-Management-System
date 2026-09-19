package com.practice.employee_ms.controller;

import com.practice.employee_ms.dto.auth.LoginRequest;
import com.practice.employee_ms.dto.auth.LoginResponse;
import com.practice.employee_ms.dto.auth.RegisterRequest;
import com.practice.employee_ms.jwt.JwtService;
import com.practice.employee_ms.model.User;
import com.practice.employee_ms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<String> signUp(@Valid @RequestBody RegisterRequest request){
        String msg= userService.signUp(request);
        return ResponseEntity.ok().body(msg);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> SignIn(@Valid @RequestBody LoginRequest request){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                         request.getPassword())
        );
        //System.out.println(authentication.getPrincipal());

        UserDetails userDetails= (UserDetails) authentication.getPrincipal();

        String token= jwtService.generateToken(userDetails);

        LoginResponse response = new LoginResponse(token,"Bearer");
        return ResponseEntity.ok(response);
    }

    
}
