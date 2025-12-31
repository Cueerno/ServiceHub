package com.radiuk.user_management_service.dto;

import java.time.Instant;

public record UserResponseDto(

        String username,
        String firstname,
        String lastname,
        String phoneNumber,
        String email,
        Instant createdAt
) {
}
