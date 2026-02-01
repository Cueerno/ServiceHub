package com.radiuk.user_management_service.service.impl;

import com.radiuk.user_management_service.entity.RefreshToken;
import com.radiuk.user_management_service.repository.RefreshTokenRepository;
import com.radiuk.user_management_service.service.RefreshTokenQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenQueryServiceImpl implements RefreshTokenQueryService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Cacheable(key = "#jti", value = "refreshToken")
    public Optional<RefreshToken> getByJtiAndRevokedFalse(String jti) {
        return refreshTokenRepository.findByJtiAndRevokedFalse(jti);
    }
}