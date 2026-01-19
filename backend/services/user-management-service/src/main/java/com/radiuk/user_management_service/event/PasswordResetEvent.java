package com.radiuk.user_management_service.event;

import java.time.Instant;

public record PasswordResetEvent(

        Long userId,
        String email,
        String token,
        Instant expiresAt
) {
}
