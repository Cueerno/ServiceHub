package com.radiuk.user_management_service.dto;

import java.time.Instant;

public record UserResponseDto(

        String firstname,
        String lastname,
        String password,
        String phoneNumber,
        String email,
        Instant createdAt
) {
}
