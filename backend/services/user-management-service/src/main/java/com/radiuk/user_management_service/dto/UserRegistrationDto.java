package com.radiuk.user_management_service.dto;

public record UserRegistrationDto(

        String username,
        String firstname,
        String lastname,
        String password,
        String phoneNumber,
        String email
) {
}
