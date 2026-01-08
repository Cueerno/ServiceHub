package com.radiuk.user_management_service.dto;

public record AuthResponse(

        String accessToken,
        String refreshToken
) {
}
