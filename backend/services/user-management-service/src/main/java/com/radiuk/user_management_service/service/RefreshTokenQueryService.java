package com.radiuk.user_management_service.service;

import com.radiuk.user_management_service.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenQueryService {

    Optional<RefreshToken> getByJtiAndRevokedFalse(String jti);
}