package com.radiuk.user_management_service.dto;

public record ResetPasswordDto(

        String oldPassword,
        String newPassword
) {
}
