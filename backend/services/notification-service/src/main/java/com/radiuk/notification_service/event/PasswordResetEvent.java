package com.radiuk.notification_service.event;

import java.time.Instant;

public record PasswordResetEvent(

        Long userId,
        String email,
        String token,
        Instant expiresAt
) {
}
