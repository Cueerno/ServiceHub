package com.radiuk.user_management_service.dto;

import com.radiuk.user_management_service.annotation.ValidLogin;
import com.radiuk.user_management_service.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record UserAuthDto(

        @ValidLogin
        @NotBlank(message = "Login is required")
        String login,

        @ValidPassword
        @NotBlank(message = "Password is required")
        String password
) {
}
