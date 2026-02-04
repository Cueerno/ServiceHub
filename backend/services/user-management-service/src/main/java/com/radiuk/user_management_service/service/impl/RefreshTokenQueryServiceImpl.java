package com.radiuk.user_management_service.service.impl;

import com.radiuk.user_management_service.repository.RefreshTokenRepository;
import com.radiuk.user_management_service.service.RefreshTokenQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenQueryServiceImpl implements RefreshTokenQueryService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Cacheable(
            key = "#jti",
            value = "validRefreshJti",
            unless = "#result == false"
    )
    public boolean isJtiValid(String jti) {
        return refreshTokenRepository
                .findByJtiAndRevokedFalse(jti)
                .isPresent();
    }
}