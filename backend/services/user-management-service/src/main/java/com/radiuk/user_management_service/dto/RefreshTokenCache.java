package com.radiuk.user_management_service.dto;

import java.time.Instant;

public record RefreshTokenCache(

        Long userId,
        boolean revoked,
        Instant expiresAt
) {
}
