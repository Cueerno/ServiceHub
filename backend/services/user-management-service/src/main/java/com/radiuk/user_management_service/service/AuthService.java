package com.radiuk.user_management_service.service;

import com.radiuk.user_management_service.dto.*;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthService {

    UserResponseDto register(UserRegistrationDto userRegistrationDto);

    AuthResponse authenticate(UserAuthDto dto);

    void resetPasswordRequest(PasswordResetRequestDto passwordResetRequestDto);

    void resetPasswordConfirm(PasswordResetConfirmDto passwordResetConfirmDto);

    void logout(Jwt jwt);
}
