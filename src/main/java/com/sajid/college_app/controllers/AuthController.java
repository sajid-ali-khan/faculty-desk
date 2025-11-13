package com.sajid.college_app.controllers;

import com.sajid.college_app.dtos.JwtResponse;
import com.sajid.college_app.dtos.LoginRequest;
import com.sajid.college_app.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        Optional<JwtResponse> jwtResponse;
        log.info("{} attempted a login", loginRequest.getFacultyCode());
        jwtResponse = authService.loginUser(loginRequest);
        log.info("{} logged in.", loginRequest.getFacultyCode());
        return ResponseEntity.ok(jwtResponse);
    }
}
