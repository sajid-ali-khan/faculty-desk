package com.sajid.college_app.services;

import com.sajid.college_app.models.RefreshToken;
import com.sajid.college_app.repositories.FacultyRepository;
import com.sajid.college_app.repositories.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${app.refreshExpirationMs}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final FacultyRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, FacultyRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(int userId) {
        var token = refreshTokenRepository.findByFaculty_Id(userId).orElseGet(RefreshToken::new);
        token.setFaculty(userRepository.findById(userId).get());
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(token);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
}
