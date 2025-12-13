package com.sajid.college_app.controllers;

import com.sajid.college_app.dtos.JwtResponse;
import com.sajid.college_app.dtos.LoginRequest;
import com.sajid.college_app.jwt.JwtUtils;
import com.sajid.college_app.repositories.RefreshTokenRepository;
import com.sajid.college_app.services.AuthService;
import com.sajid.college_app.services.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        Optional<JwtResponse> jwtResponse;
        log.info("{} attempted a login", loginRequest.getFacultyCode());
        jwtResponse = authService.loginUser(loginRequest);
        log.info("{} logged in.", loginRequest.getFacultyCode());
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {
        String requestToken = payload.get("refreshToken");
        log.info("{} requested token refresh", requestToken);
        return refreshTokenRepository.findByToken(requestToken)
                .map(token -> {
                    if (refreshTokenService.isTokenExpired(token)) {
                        refreshTokenRepository.delete(token);
                        return ResponseEntity.badRequest().body("Refresh token expired. Please login again.");
                    }
                    String newJwt = jwtUtils.generateTokenFromUsername(token.getFaculty());
                    return ResponseEntity.ok(Map.of("token", newJwt));
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {
        String requestToken = payload.get("refreshToken");
        log.info("Logout requested for token: {}", requestToken);
        if (requestToken == null || requestToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh token is required.");
        }

        return refreshTokenRepository.findByToken(requestToken)
                .map(token -> {
                    refreshTokenRepository.delete(token);
                    return ResponseEntity.ok("Logged out successfully.");
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }
}
