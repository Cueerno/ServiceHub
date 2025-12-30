package com.radiuk.user_management_service.dto;

public record UserUpdateDto(

        String username,
        String firstname,
        String lastname,
        String phoneNumber,
        String email
) {
}
