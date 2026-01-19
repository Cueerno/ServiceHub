package com.radiuk.user_management_service.dto;

public record AuthResponse(

        JwtWithJti accessToken,
        String refreshToken
) {
}
