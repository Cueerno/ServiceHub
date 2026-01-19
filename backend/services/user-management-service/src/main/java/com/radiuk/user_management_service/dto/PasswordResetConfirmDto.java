package com.radiuk.user_management_service.dto;

public record PasswordResetConfirmDto(

        String token,
        String newPassword
) {
}
