package com.radiuk.user_management_service.dto;

public record JwtWithJti(

        String accessToken,
        String jti
) {}
