package com.radiuk.user_management_service.dto;

import com.radiuk.user_management_service.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetConfirmDto(

        String token,

        @ValidPassword
        @NotBlank(message = "Password is required")
        String newPassword
) {
}
