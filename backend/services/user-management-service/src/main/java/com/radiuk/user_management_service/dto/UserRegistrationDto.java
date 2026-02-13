package com.radiuk.user_management_service.dto;

import com.radiuk.user_management_service.annotation.ValidPassword;
import jakarta.validation.constraints.*;

public record UserRegistrationDto(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Firstname is required")
        @Size(min = 2, max = 50, message = "Firstname must be between 2 and 50 characters")
        String firstname,

        @NotBlank(message = "Lastname is required")
        @Size(min = 2, max = 50, message = "Lastname must be between 2 and 50 characters")
        String lastname,

        @ValidPassword
        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^\\+?[0-9]{7,15}$",
                message = "Enter phone number in international format: digits only, optional '+', 7–15 digits (example: +375291234567)"
        )
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email

) {}