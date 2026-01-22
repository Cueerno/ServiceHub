package com.radiuk.user_management_service.service.impl;

import com.radiuk.user_management_service.entity.RefreshToken;
import com.radiuk.user_management_service.entity.User;
import com.radiuk.user_management_service.repository.RefreshTokenRepository;
import com.radiuk.user_management_service.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-ttl}")
    private Duration refreshTokenTtl;

    @Override
    @Transactional
    public String createRefreshToken(User user, String jti) {
        log.debug("Creating refresh token for user with email {}", user.getEmail());

        String rawToken = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(passwordEncoder.encode(rawToken));
        refreshToken.setJti(jti);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(refreshTokenTtl));

        refreshTokenRepository.save(refreshToken);

        log.info("Created refresh token for user with email {}", user.getEmail());
        return rawToken;
    }

    @Override
    @Transactional
    public User validateAndGetUser(String refreshToken, String jti) {
        RefreshToken token = refreshTokenRepository.findByJtiAndRevokedFalse(jti)
                .orElseThrow(() -> new JwtException("Invalid refresh token"));

        log.debug("Validating refresh token for user with email{}", token.getUser().getEmail());

        if (token.isRevoked()) {
            refreshTokenRepository.revokeAllByUser(token.getUser());
            throw new JwtException("Refresh token reuse detected");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new JwtException("Refresh token expired");
        }

        token.setRevoked(true);

        log.info("Refresh token revoked for user with email {}", token.getUser().getEmail());
        return token.getUser();
    }

    @Override
    @Transactional
    public void revokeByJti(String jti) {
        log.debug("Revoke refresh token for user {}", jti);
        refreshTokenRepository.revokeByJti(jti);
    }
}
