package com.radiuk.user_management_service.service;

import com.radiuk.user_management_service.dto.*;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthService {

    UserResponseDto register(UserRegistrationDto userRegistrationDto);

    AuthResponse authenticate(UserAuthDto dto);

    void resetPassword(ResetPasswordDto resetPasswordDto, Jwt jwt);

    void logout(Jwt jwt);
}
