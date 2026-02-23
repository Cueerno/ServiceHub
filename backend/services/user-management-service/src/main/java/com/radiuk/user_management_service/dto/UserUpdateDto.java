package com.radiuk.user_management_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(

        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Size(min = 2, max = 50, message = "Firstname must be between 2 and 50 characters")
        String firstname,

        @Size(min = 2, max = 50, message = "Lastname must be between 2 and 50 characters")
        String lastname,

        @Pattern(
                regexp = "^\\+?[0-9]{7,15}$",
                message = "Enter phone number in international format: digits only, optional '+', 7–15 digits (example: +375291234567)"
        )
        String phoneNumber,

        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email
) {
}
