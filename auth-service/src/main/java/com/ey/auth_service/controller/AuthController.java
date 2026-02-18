package com.ey.auth_service.controller;

import com.ey.auth_service.dto.AuthResponse;
import com.ey.auth_service.dto.LoginRequest;
import com.ey.auth_service.dto.RegisterRequest;
import com.ey.auth_service.entity.User;
import com.ey.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authservice;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request){
        authservice.register(request);
        return "User Registered successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authservice.login(request);
    }
}
